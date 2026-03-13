package com.rpc.example.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch 配置 - 供 netty-rpc-provider 搜索服务使用
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${spring.data.elasticsearch.uris:http://localhost:9200}")
    private String uris;

    @Bean
    public RestClient restClient() {
        try {
            String uri = uris.split(",")[0].trim();
            String url = uri.replace("http://", "").replace("https://", "");
            String[] hostPort = url.split(":");
            String host = hostPort[0];
            int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 9200;

            RestClient restClient = RestClient.builder(new HttpHost(host, port, "http")).build();
            try {
                org.elasticsearch.client.Request request = new org.elasticsearch.client.Request("GET", "/");
                org.elasticsearch.client.Response response = restClient.performRequest(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    System.out.println("✓ Elasticsearch 连接成功: " + host + ":" + port);
                }
            } catch (Exception e) {
                System.err.println("警告: Elasticsearch 连接测试失败，搜索功能可能不可用。");
            }
            return restClient;
        } catch (Exception e) {
            System.err.println("创建 Elasticsearch RestClient 失败: " + e.getMessage());
            throw new RuntimeException("无法连接 Elasticsearch", e);
        }
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(objectMapper);
        ElasticsearchTransport transport = new RestClientTransport(restClient, jsonpMapper);
        return new ElasticsearchClient(transport);
    }
}
