package by.korovkin.restClient;

public interface AsyncActivity {

	public void showLoadingProgressDialog();

	public void showProgressDialog(CharSequence message);

	public void dismissProgressDialog();

}
