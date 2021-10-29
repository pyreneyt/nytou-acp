package io.nytou.web.acp.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.nytou.web.acp.user.BackendUser;
import io.nytou.web.acp.user.UserData;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBDatabase {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;


    private Thread databaseThread;

    private final ExecutorService queue;

    private int port;
    private String database;
    private String hostName;

    public MongoDBDatabase(int port, String database, String hostName)
    {
        this.port = port;
        this.database = database;
        this.hostName = hostName;

        ThreadFactory threadFactory = (Runnable r) ->{
            Thread thread = new Thread(r, "Database Queue");
            thread.setDaemon(true);
            this.databaseThread = thread;
            return thread;
        };

        this.queue = Executors.newSingleThreadExecutor(threadFactory);
    }



    public void connect()
    {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%d", getHostName(), getPort())))
                .codecRegistry(codecRegistry)
                .build();

        this.mongoClient = MongoClients.create(clientSettings);
        this.mongoDatabase = mongoClient.getDatabase(getDatabase());

        System.out.println("[DatabaseModule] -> MongoDB connected.");
    }

    public void disconnect()
    {
        mongoClient.close();
        System.out.println("[DatabaseModule] -> MongoDB disconnected.");
    }

    public List<BackendUser>  getBackendUsers() {
        MongoCollection<BackendUser> collection = this.mongoDatabase.getCollection("acp-users", BackendUser.class);

         List<BackendUser> users = new ArrayList<>();

        for (BackendUser backendUser : collection.find()) {
            users.add(backendUser);
        }
        return users;
    }


    public boolean documentExists(String databaseCollection, String key, String value) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(databaseCollection);
        FindIterable<Document> iterable = collection.find(new Document(key, value));
        return iterable.first() != null;
    }


    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }

    public String getDatabase() {
        return database;
    }

    public void runDatabaseAction(Runnable action) {
        if (this.isDatabaseThread()) {
            action.run();
        } else {
            this.queue.execute(action);
        }
    }


    public boolean isDatabaseThread() {
        return (Thread.currentThread() == this.databaseThread);
    }

}
