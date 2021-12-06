package com.example.sources.feign;

import com.example.sources.exception.OpenFeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignError implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch(response.status()) {
            case 404:
                if(methodKey.contains("existsFile")) {
                    return new OpenFeignException("ip 혹은 url 이 존재하지 않습니다.");
                }
        }

        return null;
    }
}
