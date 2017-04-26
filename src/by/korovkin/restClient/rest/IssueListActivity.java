package by.korovkin.restClient.rest;

import java.util.List;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import by.korovkin.restClient.AbstractAsyncListActivity;
import by.korovkin.restClient.entity.Issue;
import by.korovkin.restClient.rest.adapter.IssueListAdapter;
import by.korovkin.restClient.rest.constants.Constants;
import by.korovkin.restClient.rest.service.IssueService;

public class IssueListActivity extends AbstractAsyncListActivity {

    protected static final String TAG = IssueListActivity.class.getSimpleName();
    private IssueService issueService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        issueService = new IssueService(getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        new DownloadIssues().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(l.getContext(), IssueActivity.class);
        long idd = ((Issue) l.getItemAtPosition(position)).getId();
        intent.putExtra(Constants.ISSUE_ID, idd);
        startActivity(intent);

    }

    /**
     * Display given List on activity
     * @param issues List of issues
     */
    private void refreshIssues(List<Issue> issues) {
        if (issues == null) {
            return;
        }
        IssueListAdapter adapter = new IssueListAdapter(this, issues);
        setListAdapter(adapter);
    }

    /**
     * Fetch list of issues
     */
    private class DownloadIssues extends AsyncTask<Void, Void, List<Issue>> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected List<Issue> doInBackground(Void... params) {
            try {
                return issueService.getIssues();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Issue> result) {
            dismissProgressDialog();
            refreshIssues(result);
        }

    }

}
