package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.listeners.EndlessScrollListener;
import com.codepath.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    GridView gvResults;
  //  Button btnSearch;
    Toolbar toolbar;

    private final int REQUEST_CODE = 20;

    List<Article> articles;
    ArticleArrayAdapter adapter;

    final String API_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    final String API_KEY = "faa88df7883d422989311a3f585fe6fc";

    private static ArrayList<String> fq;
    private static String sort;
    private static String date;

    private static String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
    }

    private void setupViews() {
        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        //etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                callAPI(page, currentQuery);
                return true;
            }
        });

        //btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up a listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                // get the article to display
                Article article = articles.get(position);
                // pass in that article into intent
                i.putExtra("url", article.getWebURL());
                // launch the activity
                startActivity(i);
            }
        });
    }

    /**
     * Processes result from Edit activity
     *
     * @param requestCode - the request code to edit activitys
     * @param resultCode - result code set by edit activity
     * @param data - data passed from edit activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            fq = data.getExtras().getStringArrayList("fq");
            sort = data.getExtras().getString("sort");
            date = data.getExtras().getString("date");
        }

        if(currentQuery != null) {
            callAPI(0, currentQuery);
        }
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                currentQuery = query;

                // perform query here
                callAPI(0, query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              /*  if(newText.length() > 2) {
                    callAPI(newText);
                }
                */
                return false;
            }
        });


        // on filter icon click in the menu
        MenuItem miFilter = menu.findItem(R.id.miFilter);
        miFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // create an intent to display the article
                Intent i = new Intent(getApplicationContext(), FilterActivity.class);

                i.putExtra("fq", fq);
                i.putExtra("sort", sort);
                i.putExtra("date", date);
                // launch the activity
                startActivityForResult(i, REQUEST_CODE);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    public void callAPI (final int page, String query) {
        //Toast.makeText(this, "Search for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("page", page);
        params.put("q", query);

        if(sort != null){
            params.put("sort", sort.toLowerCase());
        }

        if(fq != null && fq.size() > 0)  {
            String fqFilter = "news_desk:(\"" + TextUtils.join("\" \"", fq) + "\")";
            params.put("fq", fqFilter);
        }
        if(date != null) {
            params.put("begin_date", date);
        }

        client.get(API_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("DEBUG", articleJsonResults.toString());
                    if(page == 0) {
                        adapter.clear();
                    }
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

   /* public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        callAPI(query);
    }
    */

}
