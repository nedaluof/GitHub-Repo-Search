package com.nedal.githubreposearch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.nedal.githubreposearch.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By : nidalouf 2/26/2020
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_search_box)
    EditText mSearchBoxEditText;
    @BindView(R.id.tv_url_display)
    TextView mUrlDisplayTextView;
    @BindView(R.id.tv_github_search_results_json)
    TextView mSearchResultsTextView;
    @BindView(R.id.tv_error_message_display)
    TextView tv_err_display;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pb_loading_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void showJsonDataView() {
        tv_err_display.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        tv_err_display.setVisibility(View.VISIBLE);
    }
    public void makeGithubSearchQuery() {
        String searchQuery = mSearchBoxEditText.getText().toString();
        URL githubQueryUrl = NetworkUtils.buildUrl(searchQuery);
        mUrlDisplayTextView.setText(githubQueryUrl.toString());
        new GithubQueryTask().execute(githubQueryUrl);
    }
    public class GithubQueryTask extends AsyncTask<URL,Void,String>{
        /*
        * in AsyncTask Params first one take the params that will process
        * , second one will carry progress , third one will be the returned results
        * */
        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPreExecute() {
            pb_loading_indicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            pb_loading_indicator.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                showJsonDataView();
                mSearchResultsTextView.setText(s);
            }else {
                showErrorMessage();
            }
        }
    }
}