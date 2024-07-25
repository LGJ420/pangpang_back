package com.example.pangpang.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    /*
     * ModelMapper는 DTO <-> 엔티티 처리를
     * 수월하게 해주는 라이브러리입니다
     * 이거 Bean에다 등록해야 쓸수있습니다
     * 나중에 어떻게 쓰는지 service단에서 보실수 있습니다
     * 
     * 왠만한건 service에서 찾으면 된다고 생각하시면 됩니다
     * DTO를 엔티티로 바꿔야되고
     * 엔티티를 DTO로 바꿔야되고
     * 그게 다 서비스에서 하는거니까요
     * (서비스가 뭐에요? -> 조물딱조물딱 요리하는곳이라고 생각하시면 됩니다)
     */
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
