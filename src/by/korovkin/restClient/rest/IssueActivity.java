package by.korovkin.restClient.rest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import by.korovkin.restClient.AbstractAsyncActivity;
import by.korovkin.restClient.R;
import by.korovkin.restClient.entity.Entity;
import by.korovkin.restClient.entity.Issue;
import by.korovkin.restClient.entity.Project;
import by.korovkin.restClient.entity.Reference;
import by.korovkin.restClient.entity.User;
import by.korovkin.restClient.rest.adapter.ReferenceSpinnerAdaper;
import by.korovkin.restClient.rest.constants.Constants;
import by.korovkin.restClient.rest.service.IssueService;

public class IssueActivity extends AbstractAsyncActivity {

    List<Reference> priority;
    List<Reference> type;
    List<Reference> status;
    List<Project> project;
    List<User> user;
    Issue issue;
    IssueService issueService;

    protected static final String TAG = IssueActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_activity);
        issueService = new IssueService(getApplicationContext());

        new GetIssueTask().execute((int) getIntent().getExtras().getLong(Constants.ISSUE_ID));

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new PostIssueTask().execute();
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteIssueTask().execute();
                finish();
            }
        });
        ((AutoCompleteTextView) findViewById(R.id.edit_project)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                issue.setProject((Project) adapterView.getItemAtPosition(i));
            }
        });

        ((AutoCompleteTextView) findViewById(R.id.edit_assignee)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                issue.setUser((User) adapterView.getItemAtPosition(i));
            }
        });
    }


    /**
     * Display fetched issue_activity
     */
    private void show() {
        showReferences();
        if (issue == null) {
            Toast.makeText(this, "I got null, something happened!", Toast.LENGTH_LONG).show();
            return;
        }
        TextView t = (TextView) findViewById(R.id.id);
        t.setText(issue.getId().toString());

        EditText e = (EditText) findViewById(R.id.summary);
        e.setText(issue.getSummary());

        ((AutoCompleteTextView) findViewById(R.id.edit_project)).setText(issue.getProject().toString());

        ((Spinner) findViewById(R.id.edit_priority)).setSelection(findReferenceId(issue.getPriority(), priority));

        ((AutoCompleteTextView) findViewById(R.id.edit_assignee)).setText(issue.getUser().toString());

        ((Spinner) findViewById(R.id.edit_type)).setSelection(findReferenceId(issue.getType(), type));

        ((Spinner) findViewById(R.id.edit_status)).setSelection(findReferenceId(issue.getStatus(), status));
    }

    /**
     * Fill spinners
     */
    private void showReferences() {
        ((Spinner) findViewById(R.id.edit_priority)).setAdapter(new ReferenceSpinnerAdaper(this, priority));
        ((Spinner) findViewById(R.id.edit_type)).setAdapter(new ReferenceSpinnerAdaper(this, type));
        ((Spinner) findViewById(R.id.edit_status)).setAdapter(new ReferenceSpinnerAdaper(this, status));

        ArrayAdapter<Project> projectArrayAdapter = new ArrayAdapter<Project>(this,
                android.R.layout.simple_dropdown_item_1line, project);
        ((AutoCompleteTextView) findViewById(R.id.edit_project)).setAdapter(projectArrayAdapter);

        ArrayAdapter<User> userArrayAdapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_dropdown_item_1line, user);
        ((AutoCompleteTextView) findViewById(R.id.edit_assignee)).setAdapter(userArrayAdapter);
    }

    /**
     * Helper for spinner. Search position of reference in List
     * @param reference reference
     * @param references List of references
     * @return position of reference in List
     */
    private int findReferenceId(Entity reference, List references) {
        for (int i = 0; i < references.size(); i++) {
            if (reference.getId().equals(references.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Update current issue_activity
     */
    private class PostIssueTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
            issue.setSummary(((EditText) findViewById(R.id.summary)).getText().toString());
            issue.setPriority((Reference) ((Spinner) findViewById(R.id.edit_priority)).getSelectedItem());
            issue.setType((Reference) ((Spinner) findViewById(R.id.edit_type)).getSelectedItem());
            issue.setStatus((Reference) ((Spinner) findViewById(R.id.edit_status)).getSelectedItem());

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                issueService.postIssue(issue);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
        }

    }

    /**
     * Fetch chosen issue_activity
     */
    private class GetIssueTask extends AsyncTask<Integer, Void, Issue> {

        @Override
        protected void onPostExecute(Issue issue) {
            dismissProgressDialog();
            show();
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Issue doInBackground(Integer... voids) {
            try {
                priority = issueService.getPriorities();

                type = issueService.getTypes();

                status = issueService.getStatuses();

                project = issueService.getProjects();

                user = issueService.getUsers();

                issue = issueService.getIssue(voids[0]);
                return issue;

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * Send delete HttpMethod on current issue_activity
     */
    private class DeleteIssueTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                issueService.deleteIssue(issue.getId());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }
    }
}
