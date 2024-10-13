package com.book.BookProject;

import com.book.BookProject.order.PaymentInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    @Value("${iamport.api_key}")
    private String apiKey;

    @Value("${iamport.api_secret}")
    private String apiSecret;

    @PostMapping("/verifyPayment")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody Map<String, String> request) {
        String impUid = request.get("imp_uid");
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 아임포트 서버에서 액세스 토큰을 발급받습니다.
            String accessToken = getIamportAccessToken();

            // 2. 액세스 토큰을 사용해 결제 정보를 조회합니다.
            PaymentInfo paymentInfo = getPaymentData(impUid, accessToken);

            // 3. 결제 검증 로직 추가 (예: 결제 금액과 서버에 저장된 주문 금액이 일치하는지 확인)
            if (paymentInfo != null && paymentInfo.getAmount() > 0) {
                response.put("success", true);
                response.put("paymentInfo", paymentInfo);
            } else {
                response.put("success", false);
                response.put("message", "결제 검증 실패");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "결제 검증 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refundPayment")
    public ResponseEntity<Map<String, Object>> refundPayment(@RequestBody Map<String, String> request) {
        String impUid = request.get("imp_uid"); // 환불할 결제의 고유 ID
        Integer amount = Integer.parseInt(request.get("amount")); // 환불할 금액
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 아임포트 서버에서 액세스 토큰을 발급받습니다.
            String accessToken = getIamportAccessToken();

            // 2. 환불 요청을 보냅니다.
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.iamport.kr/payments/cancel"; // 아임포트 환불 API

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken); // 발급받은 액세스 토큰을 헤더에 추가
            headers.setContentType(MediaType.APPLICATION_JSON); // 요청 본문의 타입 설정

            // 요청 데이터 준비
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("imp_uid", impUid); // 환불할 결제의 imp_uid
            requestBody.put("amount", amount); // 환불할 금액 (전체 환불 시 생략 가능)

            // 요청을 보냅니다.
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> apiResponse = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> responseBody = apiResponse.getBody();

            // 3. 환불 처리 결과를 확인합니다.
            if (responseBody != null && responseBody.get("code") != null && ((Integer) responseBody.get("code")) == 0) {
                response.put("success", true);
                response.put("message", "환불이 성공적으로 처리되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "환불 처리에 실패했습니다. 아임포트 응답: " + responseBody);
            }
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "환불 요청 중 통신 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "환불 요청 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getIamportAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/users/getToken";

        // 요청 본문 데이터 준비
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("imp_key", apiKey);
        requestBody.put("imp_secret", apiSecret);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 설정
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 아임포트에 액세스 토큰 요청
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // 응답 검증
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.get("code") != null && ((Integer) responseBody.get("code")) == 0) {
                Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
                return (String) responseData.get("access_token");
            }

            throw new RuntimeException("Failed to get Iamport access token: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred while requesting Iamport access token: " + e.getMessage());
        }
    }

    private PaymentInfo getPaymentData(String impUid, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.get("code") != null && ((Integer) responseBody.get("code")) == 0) {
            Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
            return new PaymentInfo(
                    (String) responseData.get("imp_uid"),
                    (String) responseData.get("merchant_uid"),
                    ((Number) responseData.get("amount")).intValue()
            );
        }

        throw new RuntimeException("Failed to get payment data from Iamport");
    }
}