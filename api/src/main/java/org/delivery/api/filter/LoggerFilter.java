package org.delivery.api.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        var req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        var res = new ContentCachingResponseWrapper((HttpServletResponse) response);
        // 형번환 후 ContentCachingRequestWrapper의 형태로 뒷 단의 intercepter라던지 controller들이 받게 끔

        log.info("Looger 진입 >>> {}", req.getRequestURI());

        // 필터 실행 전
        // 필터 실행전 request의 header와 body를 로그 출력하는게 Best임
        // request를 캐싱해줄 수 있는 별도의 클래스를 하나 만들어서 써야함 (나중에 기회 된다면 해본다함)

        chain.doFilter(req, res);
        // 필터 실행 후

        // **** request 정보 ****
        var headerNames = req.getHeaderNames();
        var reqHeaderValues = new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey ->{
            var headerValue = req.getHeader(headerKey);
            // authorization-token : ???, user-agent : ???
            reqHeaderValues.append("[")
                           .append(headerKey)
                           .append(" : ")
                           .append(headerValue)
                           .append("]  ");
        });

        var reqBody = new String(req.getContentAsByteArray());
        var uri = req.getRequestURI();
        var method = req.getMethod();

        log.info(">>>>> Request uri: {}, method: {}, header: {}, body: {}", uri, method, reqHeaderValues, reqBody);

        // **** response 정보 ****
        var resHeaderValues = new StringBuilder();

        res.getHeaderNames().forEach(headerKey -> {
            var headerValue = res.getHeader(headerKey);
            resHeaderValues.append("[")
                           .append(headerKey)
                           .append(" : ")
                           .append(headerValue)
                           .append("]");
        });

        var resBody = new String(res.getContentAsByteArray());
        log.info("<<<<< Response uri: {}, method: {}, header: {}, body: {}", uri, method, resHeaderValues, resBody);

        res.copyBodyToResponse(); // response의 body의 내용을 한 번 읽어버렸기 때문에 초기화
    }
}
