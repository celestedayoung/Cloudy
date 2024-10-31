package com.cloudy.global.config.swagger;

import com.cloudy.global.error.ErrorCode;
import com.cloudy.global.error.ErrorInfo;
import com.cloudy.global.response.Response;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        SwaggerApiError swaggerApiError = handlerMethod.getMethodAnnotation(SwaggerApiError.class);
        if(swaggerApiError != null){
            generateErrorCodeResponse(operation, swaggerApiError.value());
        }
        return operation;
    }

    //에러 코드로 error response 만들어 ApiResponse 에 넣음
    private void generateErrorCodeResponse(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();
        Map<Integer, List<Response<ErrorInfo>>> statusWithErrorResponse = Arrays.stream(errorCodes)
                .map(
                        Response::ERROR
                )
                .collect(Collectors.groupingBy(errorInfoResponse -> errorInfoResponse.getStatusCode().value()));
        addErrorCodesToResponse(responses, statusWithErrorResponse);
    }

    //ApiResponses에 error response 추가
    private void addErrorCodesToResponse(ApiResponses apiResponses,  Map<Integer, List<Response<ErrorInfo>>> responses) {
        responses.forEach((status, value) ->{
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();

            value.forEach(
                    errorInfoResponse -> {
                        Example example = new Example();
                        example.setValue(errorInfoResponse);
                        mediaType.addExamples(errorInfoResponse.getData().getCode(),example);
                    });

            content.addMediaType("application/json", mediaType);
            apiResponse.setContent(content);
            apiResponses.addApiResponse(String.valueOf(status), apiResponse);
        });
    }
}