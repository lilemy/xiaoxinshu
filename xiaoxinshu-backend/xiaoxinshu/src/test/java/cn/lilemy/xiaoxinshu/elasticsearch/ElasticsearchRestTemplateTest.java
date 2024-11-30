package cn.lilemy.xiaoxinshu.elasticsearch;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ElasticsearchRestTemplateTest {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    private final String INDEX_NAME = "test_index";

    @Test
    public void testCreateIndex() {
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "Elasticsearch Introduction");
        doc.put("content", "Learn Elasticsearch basics and advanced usage.");
        doc.put("tags", "elasticsearch,search");
        doc.put("answer", "Yes");
        doc.put("userId", 1L);
        doc.put("reviewId", 1L);
        doc.put("reviewStatus", true);
        doc.put("reviewMessage", "Yes");
        doc.put("reviewTime", "2023-09-01 10:00:00");
        doc.put("thumbNum", 1L);
        doc.put("favourNum", 1L);
        doc.put("viewNum", 1L);
        doc.put("priority", 1L);
        doc.put("editTime", "2023-09-01 10:00:00");
        doc.put("createTime", "2023-09-01 09:00:00");
        doc.put("updateTime", "2023-09-01 09:10:00");
        doc.put("isDelete", false);

        IndexQuery indexQuery = new IndexQueryBuilder().withId("1").withObject(doc).build();
        String documentId = elasticsearchTemplate.index(indexQuery, IndexCoordinates.of(INDEX_NAME));

        assertThat(documentId).isNotNull();
        log.info("Document ID: {}", documentId);
    }

    @Test
    public void testGetIndex() {
        String documentId = "1";

        Map<String, Object> doc = elasticsearchTemplate.get(documentId, Map.class, IndexCoordinates.of(INDEX_NAME));
        assertThat(doc).isNotNull();
        assertThat(doc.get("title")).isEqualTo("Elasticsearch Introduction");
        log.info(doc.toString());
    }

    @Test
    public void testUpdateIndex() {
        String documentId = "1";

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Elasticsearch Title");
        updates.put("updateTime", "2023-09-01 10:30:00");

        UpdateQuery updateQuery = UpdateQuery.builder(documentId)
                .withDocument(Document.from(updates))
                .build();

        elasticsearchTemplate.update(updateQuery, IndexCoordinates.of(INDEX_NAME));

        Map<String, Object> updatedDocument = elasticsearchTemplate.get(documentId, Map.class, IndexCoordinates.of(INDEX_NAME));
        assertThat(updatedDocument.get("title")).isEqualTo("Updated Elasticsearch Title");
        log.info(updatedDocument.toString());
    }

    @Test
    public void testDeleteIndex() {
        String documentId = "1";

        String result = elasticsearchTemplate.delete(documentId, IndexCoordinates.of(INDEX_NAME));
        assertThat(result).isNotNull();
        log.info(result);
    }

}