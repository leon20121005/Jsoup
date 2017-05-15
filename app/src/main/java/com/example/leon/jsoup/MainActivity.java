package com.example.leon.jsoup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private Button _parseButton;
    private Button _clearButton;
    private TextView _textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _textView = (TextView) findViewById(R.id.textView);
        _parseButton = (Button) findViewById(R.id.button2);
        _parseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GetWebSite();
            }
        });
        _clearButton = (Button) findViewById(R.id.button);
        _clearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _textView.setText("Cleared");
            }
        });
    }

    private void GetWebSite()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final StringBuilder builder = new StringBuilder();

                try
                {
                    Document doc = Jsoup.connect("http://zineblog.com.tw/blog/post/45501739").get();
                    String title = doc.title();
                    Elements links = doc.select("a[href]:contains(【)");

                    builder.append(title).append("\n");

                    for (Element link : links)
                    {
                        builder.append("\n").append("Link: ").append(link.attr("href")).append("\n").append("Text: ").append(link.text());
                    }
                }
                catch (IOException e)
                {
                    builder.append("Error: ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _textView.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}