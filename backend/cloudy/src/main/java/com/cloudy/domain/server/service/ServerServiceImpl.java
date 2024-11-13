package com.cloudy.domain.server.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.cloudy.domain.instance.model.Instance;
import com.cloudy.domain.instance.repository.InstanceRepository;
import com.cloudy.domain.member.model.Member;
import com.cloudy.domain.member.repository.MemberRepository;
import com.cloudy.domain.server.model.Server;
import com.cloudy.domain.server.model.dto.request.*;
import com.cloudy.domain.server.model.dto.response.*;
import com.cloudy.domain.server.repository.ServerRepository;
import com.cloudy.domain.serviceusage.model.ServiceUsage;
import com.cloudy.domain.serviceusage.repository.ServiceUsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;
    private final InstanceRepository instanceRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final ServiceUsageRepository serviceUsageRepository;

    @Override
    public ServerResponse createServer(ServerCreateRequest request, Long memberId) {
//        System.out.println(request.getInstanceType() + " " +request.getPaymentType());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Instance instance = instanceRepository.findByInstanceNameAndInstancePeriodType(request.getInstanceType(), request.getPaymentType())
                .orElseThrow(() -> new IllegalArgumentException("Invalid instance type"));

        Server server = new Server(request.getServerName(), 0, request.getPaymentType(), member, instance);
        serverRepository.save(server);

        return null;
    }

    @Override
    public ThresholdResponse updateThreshold(ThresholdUpdateRequest request, Long memberId) {
        Server server = serverRepository.findById(request.getServerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid server ID"));

        // 서버 임계치 설정
        server.setServerLimit(request.getUpdatedLimitValue());

        // 응답 생성
        return new ThresholdResponse(server.getServerId(), server.getServerName() , server.getServerLimit(), request.isUseAlarm(), LocalDateTime.now());
    }

    @Override
    public List<ServerResponse> getServers(Long memberId) {
        List<Server> servers = serverRepository.findByMember_MemberId(memberId);

        return servers.stream()
                .map(ServerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ServerDetailResponse getServerDetail(Long serverId) {

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid server ID"));

        return new ServerDetailResponse(
                server.getServerId(),
                server.getServerName(),
                server.getInstance().getCloudType(),
                server.getInstance().getInstanceName(),
                server.getPaymentType(),
                server.getCreatedAt()
        );
    }

    @Override
    public ServerResponse deleteServer(Long serverId, Long memberId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid server ID"));

        // 멤버 권한 확인
        if (!server.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("Unauthorized member ID");
        }

        // 서버 삭제
        serverRepository.delete(server);

        return ServerResponse.fromEntity(server);
    }

    // todo: 얘는 나중에 데이터 넣어보고 구현해봐야됨
    @Override
    public MonitoringResponse monitorServer(Long serverId, int duration) {



        return null;
    }

    @Override
    public List<ThresholdResponse> getThresholds(Long memberId) {
        List<Server> servers = serverRepository.findByMember_MemberId(memberId);

        // 2. DTO로 변환하여 반환
        return servers.stream()
                .map(server -> ThresholdResponse.builder()
                        .serverId(server.getServerId())
                        .serverName(server.getServerName())
                        .serverLimit(server.getServerLimit())
                        .updatedAt(server.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CpuUsage getCPUData(Long containerId) throws IOException {
        // process Builder로 docker image stats 가져오기
        String[] commands = {"top", "-b", "-n", "1"}; // 이건 나중에 하드코딩 풀어야함.

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process result = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(result.getInputStream()));
        reader.readLine(); // 첫번째 줄 날리기
        String line;
        CpuUsage usage = new CpuUsage();
        boolean memoryCheck = false;
        while ((line = reader.readLine()) != null) {
            System.out.println("cur Line : " + line);
            if (line.contains("%Cpu(s):")) {
                // CPU 사용률 추출
                String[] cpuParts = line.split(",");
                String userCpu = cpuParts[0].split(":")[1].trim(); // us (사용자 모드 CPU)
                String sysCpu = cpuParts[1].trim();  // sy (시스템 모드 CPU)
                double userCpuPercent = Double.parseDouble(userCpu.split(" ")[0]);
                double sysCpuPercent = Double.parseDouble(sysCpu.split(" ")[0]);
                double cpuUsage = userCpuPercent + sysCpuPercent; // 전체 CPU 사용률
                System.out.println("user Cpu : " + userCpu + " sysCpu" + sysCpu);
                System.out.println(cpuUsage);
                usage.setCpuPercent(cpuUsage);

            }

            if (line.contains("MiB Mem :")) {
                // 메모리 사용량 추출
                String[] parts = line.split(":");
                String[] memParts = parts[1].split(",");
                // total과 free 값 추출
                String totalPart = memParts[0].trim(); // "15986.8 total"
                String usagePart = memParts[2].trim();  // "1186.5 free"
                System.out.println("total part : " + totalPart + " usagePart : " + usagePart);
                // total과 free 값에서 숫자만 추출
                double total = Double.parseDouble(totalPart.split(" ")[0].trim());
                double memuse = Double.parseDouble(usagePart.split(" ")[0].trim());
                System.out.println(total + " " + memuse);
                usage.setMemUsage(memuse);
                usage.setMemLimit(total);
                memoryCheck = true;
            }
            if (memoryCheck){
                break;
            }
        }
        return usage;
    }

    @Override
    public ServerMonthCostResponse monthServerCost(ServerMonthCostRequest request) {
        Map<String, Long> apiUsageMap = new TreeMap<>();

        // 오늘 날짜로 인덱스 설정
        String date = request.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM"));
        String searchIndex = "server-logs-" + date + "*";

        try {
            SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                            .index(searchIndex)
                            .query(q -> q
                                    .matchPhrase(m -> m
                                            .field("message")
                                            .query("external_service: true")
                                    )
                            ),
                    Map.class
            );

            for (Hit<Map> hit : searchResponse.hits().hits()) {
                // 각 히트의 소스에서 `message` 필드를 가져옴
                Map<String, Object> sourceMap = hit.source();
                if (sourceMap != null && sourceMap.containsKey("message")) {
                    String message = sourceMap.get("message").toString();
                    System.out.println("Message: " + message);

                    // message에서 API 경로 추출하여 호출 횟수를 기록
                    String apiPath = extractApiPath(message);
                    apiUsageMap.put(apiPath, apiUsageMap.getOrDefault(apiPath, 0L) + 1);
                } else {
                    System.out.println("Message field is missing or source is null");
                }
            }

        } catch (Exception e) {
            if (e.getMessage().contains("index_not_found_exception")) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }

        Server server = serverRepository.findById(Long.valueOf(request.getServerId())).orElseThrow(()-> new NotFoundException("not found"));
        Instance instance = server.getInstance();

        double accumulatedCost = instance.getCostPerHour() * 24 * request.getDate().getDayOfMonth();
        // 인스턴스 누적 비용

        double expectedCost = instance.getCostPerHour() * 24 * 30;
        // 인스턴스 예상 월 비용

        double serviceCost = 0;

        for(String api : apiUsageMap.keySet()){
            ServiceUsage serviceUsage = serviceUsageRepository.findServiceUsageByServiceName(api);
            System.out.println(api);
            Long count = apiUsageMap.get(api);
            serviceCost += serviceUsage.getServiceCost() * count;
        }

        accumulatedCost += serviceCost;
        expectedCost += (serviceCost / request.getDate().getDayOfMonth() )* 30;

        return new ServerMonthCostResponse(Double.parseDouble(String.format("%.3f", accumulatedCost))
                , Double.parseDouble(String.format("%.3f", expectedCost)));
    }

    @Override
    public ServerDailyCostResponse dailyServerCost(ServerDailyCostRequest request) {
        Map<String, Long> apiUsageMap = new TreeMap<>();

        // 오늘 날짜로 인덱스 설정
        String date = request.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String searchIndex = "server-logs-" + date + "*";

        try {
            SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                            .index(searchIndex)
                            .query(q -> q
                                    .matchPhrase(m -> m
                                            .field("message")
                                            .query("external_service: true")
                                    )
                            ),
                    Map.class
            );

            for (Hit<Map> hit : searchResponse.hits().hits()) {
                // 각 히트의 소스에서 `message` 필드를 가져옴
                Map<String, Object> sourceMap = hit.source();
                if (sourceMap != null && sourceMap.containsKey("message")) {
                    String message = sourceMap.get("message").toString();
                    System.out.println("Message: " + message);

                    // message에서 API 경로 추출하여 호출 횟수를 기록
                    String apiPath = extractApiPath(message);
                    apiUsageMap.put(apiPath, apiUsageMap.getOrDefault(apiPath, 0L) + 1);
                } else {
                    System.out.println("Message field is missing or source is null");
                }
            }

        } catch (Exception e) {
            if (e.getMessage().contains("index_not_found_exception")) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }

        Server server = serverRepository.findById(Long.valueOf(request.getServerId())).orElseThrow(()-> new NotFoundException("not found"));
        Instance instance = server.getInstance();

        double cost = instance.getCostPerHour() * 24;

        for(String api : apiUsageMap.keySet()){
            ServiceUsage serviceUsage = serviceUsageRepository.findServiceUsageByServiceName(api);
            System.out.println(api);
            Long count = apiUsageMap.get(api);
            cost += serviceUsage.getServiceCost() * count;
        }

        return new ServerDailyCostResponse(Double.parseDouble(String.format("%.3f", cost)));
    }


    // API 경로 추출 메서드
    private String extractApiPath(String message) {
        Pattern pattern = Pattern.compile("API: (/[^,]+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }

}
