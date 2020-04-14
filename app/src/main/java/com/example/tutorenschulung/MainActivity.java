package com.example.tutorenschulung;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.Security;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Editable text = editText.getText();
            int i = Integer.parseInt(text.toString());
            getFactFuture(i).thenAccept(result -> {
                runOnUiThread(() -> textView.setText(result));
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });

        });
    }

    private CompletableFuture<String> getFactFuture(int numbersesEntered) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return MainActivity.this.getFact(numbersesEntered);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getFact(int i) throws IOException {
        Request request = new Request.Builder()
                .url("http://numbersapi.com/"+i)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


}
