package by.korovkin.restClient.rest.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import by.korovkin.restClient.bean.Message;
import by.korovkin.restClient.entity.Issue;
import by.korovkin.restClient.entity.Project;
import by.korovkin.restClient.entity.Reference;
import by.korovkin.restClient.entity.User;
import by.korovkin.restClient.rest.constants.Constants;

import static android.content.ContentValues.TAG;

public class IssueService {

    private SharedPreferences pref;

    public IssueService(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public Issue getIssue(int id) {
        String url = Constants.BASE_URL + "/issue/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), Issue.class).getBody();
    }

    public List<Reference> getPriorities() {
        return getReferences(Constants.BASE_URL + "/issue/priority");
    }

    public List<Reference> getTypes() {
        return getReferences(Constants.BASE_URL + "/issue/type");
    }

    public List<Reference> getStatuses() {
        return getReferences(Constants.BASE_URL + "/issue/status");
    }

    private List<Reference> getReferences(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), Reference[].class).getBody());
    }

    public List<Project> getProjects() {
        String url = Constants.BASE_URL + "issue/project";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), Project[].class).getBody());
    }

    public List<User> getUsers() {
        String url = Constants.BASE_URL + "/issue/user";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), User[].class).getBody());
    }

    public List<Issue> getIssues() {
        String url = Constants.BASE_URL + "/issue";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), Issue[].class).getBody());
    }

    public void postIssue(Issue issue) {
        final String url = Constants.BASE_URL + "/issue/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(Constants.TOKEN, pref.getString(Constants.TOKEN, ""));
        HttpEntity<Issue> requestEntity = new HttpEntity<Issue>(issue, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, Issue.class);
    }

    public void deleteIssue(long id) {
        String url = Constants.BASE_URL + "/issue/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.exchange(url, HttpMethod.DELETE, getAuthHeader(), Issue.class);
    }

    public Message login(String login, String password) {
        String url = Constants.BASE_URL + "issue/1";
        HttpAuthentication authHeader = new HttpBasicAuthentication(login, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            ResponseEntity<Issue> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Issue.class);
            return new Message(response.getStatusCode(), response.getHeaders().get(Constants.TOKEN).get(0));
        } catch (HttpClientErrorException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new Message(e.getStatusCode(), "Error");
        } catch (ResourceAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new Message(HttpStatus.INTERNAL_SERVER_ERROR, "Error");
        }
    }

    public Message login(String token) {
        String url = Constants.BASE_URL + "issue/1";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            ResponseEntity<Issue> response = restTemplate.exchange(url, HttpMethod.GET, getAuthHeader(), Issue.class);
            return new Message(response.getStatusCode(), token);
        } catch (HttpClientErrorException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new Message(e.getStatusCode(), "Error");
        } catch (ResourceAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new Message(HttpStatus.INTERNAL_SERVER_ERROR, "Error");
        }
    }

    private HttpEntity<?> getAuthHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(Constants.TOKEN, pref.getString(Constants.TOKEN, ""));
        return new HttpEntity(requestHeaders);
    }
}
