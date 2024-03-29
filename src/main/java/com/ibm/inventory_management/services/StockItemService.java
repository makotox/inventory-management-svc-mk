package com.ibm.inventory_management.services;

import java.io.IOException;
import java.lang.InternalError;
import java.util.List;
import javax.annotation.PostConstruct;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.ibm.inventory_management.config.CloudantConfig;
import com.ibm.inventory_management.models.StockItem;

@Service
@Primary
public class StockItemService implements StockItemApi {
    private CloudantConfig config;
    private CloudantClient client;
    private Database db = null;

    public StockItemService(CloudantConfig config, CloudantClient client) {
        this.config = config;
        this.client = client;
    }

    @PostConstruct
    public void init() {
        db = client.database(config.getDatabaseName(), true);
    }

    @Override
    public List<StockItem> listStockItems() {

        try {
            return db.getAllDocsRequestBuilder()
                    .includeDocs(true)
                    .build()
                    .getResponse()
                    .getDocsAs(StockItem.class);

        } catch (IOException e) {
            throw new InternalError("", e);
        }
    }
}