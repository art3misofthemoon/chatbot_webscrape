package com.example.chatbot;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callWebScraping(question);
            welcomeTextView.setVisibility(View.GONE);
        });

        Intent serviceIntent = new Intent(this, WorkerService.class);
        startService(serviceIntent);
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callWebScraping(String query) {
        // Indicate that the bot is typing in the UI
        runOnUiThread(() -> addToChat("Typing...", Message.SENT_BY_BOT));

        // Use AsyncTask to perform web scraping in the background
        new WebScrapingTask().execute(query);
    }

    private class WebScrapingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Perform web scraping in the background
            return webScrape(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // Remove the "Typing..." message and add the web scraped result
            messageList.remove(messageList.size() - 1);
            addResponse(result);
        }
    }

    private String webScrape(String query) {
        try {
            // Performing a Google search and extracting information from the search results
            Document document = Jsoup.connect("https://www.google.com/search?q=" + query).get();
            Elements searchResults = document.select("div.g");
            if (!searchResults.isEmpty()) {
                Element firstResult = searchResults.first();
                // Extracting information from the first search result
                return firstResult.text();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No information found.";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
