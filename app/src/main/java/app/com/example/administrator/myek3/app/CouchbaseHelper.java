package app.com.example.administrator.myek3.app;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CouchbaseHelper {

    Manager manager = null;
    Database database = null;
    android.content.Context ctx = null;

    private static final String TAG = "CouchbaseHelper";

    /**
     * Default constructor. Generates an instance of the database manager within the context of
     * our application. This manager will handle all queries to the database and needs to be setup
     * first.
     * @param ctx
     */
    public CouchbaseHelper(android.content.Context ctx) {
        Log.d(TAG, "Database constructor called.");
        try {
            manager = new Manager(new AndroidContext(ctx), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createCouchbaseDatabase();
    }

    /**
     * Creating a database if it does not exist yet.
     */
    private void createCouchbaseDatabase() {

        String databaseName = "shoppinglist";

        Log.d(TAG, "Create database method called.");
        // Create or open the database named app.
        try {
            database = manager.getDatabase(databaseName);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is an example of how to insert a single document. Probably we will be inserting
     * complete shopping lists instead of articles so this needs to be adjusted.
     */
    public void createArticle() {
        Log.d(TAG, "Create article method called.");
        // The properties that will be saved on the document
        Map<String, Object> properties = new HashMap<>();
        properties.put("title", "Couchbase Mobile");
        properties.put("sdk", "Java");
        // Create a new document
        Document document = database.createDocument();
        // Save the document to the database.
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error while putting properties.");
            e.printStackTrace();
        }
        // Log the document ID (generated by the database).
        Log.d(TAG, String.format("Document ID: %s", document.getId()));
    }

    /**
     * When called creates a new document of a shopping list in the database and returns the
     * documents id as callback. This id needs to be stored within the shopping list object to
     * handle the object uniquely. Our ShoppingList class provides a methods to get and set this
     * id.
     * @param shoppingList
     */
    public String addShoppingList(ShoppingList shoppingList) {
        Log.d(TAG, "addShoppingList method called.");
        // The properties that will be saved on the document.
        Map<String, Object> properties = new HashMap<>();
        properties.put(shoppingList.getShoppingListName(), shoppingList);
        // Create a new document.
        Document document = database.createDocument();
        // Save the document to the database.
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error while putting properties.");
            e.printStackTrace();
        }
        // Log the document ID (generated by the database).
        Log.d(TAG, String.format("Document ID: %s", document.getId()));
        return document.getId();
    }

    /**
     * Expects two parameters firstly a shoppingList object and secondly a String containing
     * the shoppingLists id. Ensure to first insert the shoppingList into the database, otherwise
     * the shoppingList won't have an id assigned to it. The id only gets set by the database when
     * the ShoppingList gets inserted the first time.
     *
     * TODO: Revise PLS!
     * @param id
     * @return
     */
    public ShoppingList getShoppingListById(String id) {

        String listName = null;
        List<Article> articleList = new ArrayList<Article>();
        ShoppingList shoppingList;

        Log.d(TAG, "Calling getShoppingListById: " + id);
        Document doc = database.getDocument(id);
        Log.d(TAG, "Doc: " + doc);
        Map<String, Object> properties = doc.getProperties();
        Log.d(TAG, "Document properties: " + properties);
        for(Map.Entry<String, Object> entry : properties.entrySet()) {
            Log.d(TAG, "Key => " + entry.getKey());
            Log.d(TAG, "Value => " + entry.getValue());
            if(entry.getKey() != "_id" && entry.getKey() != "_rev") {
                Log.d(TAG, "List name: " + entry.getKey());
                listName = entry.getKey();
            }
        }

        Map<ShoppingList, ArrayList<Article>> myShoppingList = (Map<ShoppingList, ArrayList<Article>>) doc.getProperty(listName);
        Log.d(TAG, "ShoppingList: " + myShoppingList);

        /**
         * ArrayList of Articles.
         */
        Log.d(TAG, "Shopping list Articles class => " + myShoppingList.get("shoppingListArticles").getClass());

        /**
         * TODO: Use for sorting before calling Article constructors!!!!
         */
//        for(int i = 0; i < myShoppingList.get("shoppingListArticles").size(); i++) {
//
//            Object art11 = myShoppingList.get("shoppingListArticles").get(i);
//            Map<String, Object> articleMap1 = (Map<String, Object>) art11;
//            String amount = (String) articleMap1.get("articleAmount");
//            String name = (String) articleMap1.get("articleName");
//            String comment = (String) articleMap1.get("articleComment");
//            Double price = (Double) articleMap1.get("articlePrice");
//            Article artikel = new Article(
//                    (String) articleMap1.get("articleName"),
//                    (String) articleMap1.get("articleAmount"),
//                    null,
//                    (Double) articleMap1.get("articlePrice"),
//                    (String) articleMap1.get("articleComment"));
//            Log.d(TAG, "art11 :::::::::: " + artikel.toString());
//        }

        for(Object art : myShoppingList.get("shoppingListArticles")) {
            Log.d(TAG, "================== Article in myShoppingList ==================");
            Log.d(TAG, "art :: " + art);
            Log.d(TAG, "art 2 string:: " + art.toString());
            Log.d(TAG, "art getClass:: " + art.getClass());

            Article article = new Article();
            Map<String, Object> articleMap = (Map<String, Object>) art;
            for(Map.Entry<String, Object> entry : articleMap.entrySet()) {

                if(entry.getKey() == "articleName" && entry.getValue() != null) {
                    Log.d(TAG, "articleName: " + entry.getValue());
                    article.setArticleName(entry.getValue().toString());
                }
                if(entry.getKey() == "articleAmount" && entry.getValue() != null) {
                    Log.d(TAG, "articleAmount: " + entry.getValue());
                    article.setArticleAmount((String) entry.getValue());
                }
                if(entry.getKey() == "articleUnit" && entry.getValue() != null) {
                    Log.d(TAG, "articleUnit: " + entry.getValue());
                    article.setArticleUnit((String) entry.getValue());
                }
                if(entry.getKey() == "articlePrice" && entry.getValue() != null) {
                    Log.d(TAG, "articlePrice: " + entry.getValue());
                    article.setArticlePrice(entry.getValue().toString());
                }
                if(entry.getKey() == "articleComment" && entry.getValue() != null) {
                    Log.d(TAG, "articleComment: " + entry.getValue());
                    article.setArticleComment((String) entry.getValue());
                }
                if(entry.getKey() == "articleChecked" && entry.getValue() != null) {
                    Log.d(TAG, "articleChecked: " + entry.getValue());
                    article.setArticleChecked((Boolean) entry.getValue());
                }
            }
            articleList.add(article);
        }
        shoppingList = new ShoppingList(listName, (ArrayList<Article>) articleList);
        return shoppingList;
    }

    /**
     * This is an example on how to work on all existing documents which might be interesting
     * when working operations on the whole collection.
     */
    public void getAllDocuments() {
        Log.d(TAG, "Calling getAllDocuments.");
        // Let's find all documents and print them to the console.
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Log.d(TAG, String.format("%s. document: %s", row.getSequenceNumber(), row.getDocumentId()));

            if(row.getDocumentId() != null) {
                Document doc = database.getDocument(row.getDocumentId());
                // We can directly access properties from the document object.
                Log.d(TAG, "doc.getProperty(\"BananaList001\") " + doc.getProperty("BananaList001"));

                Map<ShoppingList, ArrayList<Article>> testObject = (Map<ShoppingList, ArrayList<Article>>) doc.getProperty("BananaList001");
                Log.d(TAG, "TESTOBJECT: " + testObject.getClass());
                /**
                 * Returns a java.util.ArrayList of articles.
                 */
                Log.d(TAG, "TESTOBJECT get shoppingListArticles: " + testObject.get("shoppingListArticles"));
            }
        }
    }
}
