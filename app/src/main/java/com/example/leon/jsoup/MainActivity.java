package com.example.leon.jsoup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private Button _parseButton;
    private Button _clearButton;
    private TextView _textView;

    private String HOME_URL = "http://zineblog.com.tw/blog/post/45501739";
    private String HOME_CONDITION = "a[href]:contains(【)";
    private String ADDRESS_CONDITION = "span:contains(地址)";

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
                _textView.setText("Parsing...");
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
                    Document doc = Jsoup.connect(HOME_URL).get();
                    String title = doc.title();
                    Elements links = doc.select(HOME_CONDITION);

                    builder.append(title).append("\n");

                    for (int i = 0; i < 20; i++)
                    {
                        builder.append("\n").append("Title: ").append(links.get(i).text()).append("\n").append("Link: ").append(links.get(i).attr("href"));
                        GetAddress(links.get(i).attr("href"), builder);
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

    private void GetAddress(String link, StringBuilder builder)
    {
        try
        {
            Document doc = Jsoup.connect(link).get();
            Elements addresses = doc.select(ADDRESS_CONDITION);

            String[] segments = addresses.get(0).toString().split("<br>");

            for (String segment : segments)
            {
                if (segment.contains("地址"))
                {
                    builder.append("\n").append("Address: ").append(segment).append("\n");
                }
            }
        }
        catch (IOException e)
        {
            builder.append("Error: ").append(e.getMessage()).append("\n");
        }
        catch (IndexOutOfBoundsException e)
        {
            builder.append("\n").append("Address: none").append("\n");
        }
    }
}