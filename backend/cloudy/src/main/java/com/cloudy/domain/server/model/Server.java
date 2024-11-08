package com.cloudy.domain.server.model;

import com.cloudy.domain.instance.model.Instance;
import com.cloudy.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "server")
@Getter
@NoArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverId;

    @Column(length = 100, nullable = false)
    private String serverName; //서버이름

    @Column(length = 100, nullable = false)
    private String serverLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id", nullable = false)
    private Instance instanceId;

}