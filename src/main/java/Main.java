import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String REMOTE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        HttpGet request = new HttpGet(REMOTE_URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        try (CloseableHttpResponse response = getHttpClient().execute(request);) {
//            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//            String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//            System.out.println(body);

            List<Fact> facts = mapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<List<Fact>>() {
                    }
            );

            facts.stream().filter(e -> e.getUpvotes() != null).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e);
        }


    }

    static CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .setUserAgent("My service")
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(5000)//макс время ожидания подключения к серверу
                                .setSocketTimeout(30000) // макс. время ожидания получения ответа
                                .setRedirectsEnabled(false).build()
                ).build();
    }
}
