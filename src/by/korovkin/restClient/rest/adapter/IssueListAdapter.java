package by.korovkin.restClient.rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import by.korovkin.restClient.R;
import by.korovkin.restClient.entity.Issue;

public class IssueListAdapter extends BaseAdapter {

	private List<Issue> issues;
	private final LayoutInflater layoutInflater;

	public IssueListAdapter(Context context, List<Issue> issues) {
		this.issues = issues;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return this.issues != null ? issues.size() : 0;
	}

	public Issue getItem(int position) {
		return this.issues.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.issue_list_item, parent, false);
		}

		Issue issue = getItem(position);
		if (issue != null) {
            TextView t = (TextView) convertView.findViewById(R.id.project_name);
            t.setText("#" + issue.getId() + " " + issue.getProject().getName());
			t = (TextView) convertView.findViewById(R.id.summary);
			t.setText("(" + issue.getType().getValue() + ") " + issue.getSummary());
		}

		return convertView;
	}

}
