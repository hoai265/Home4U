package app.hoai.bkit4u.home4u.listener;

/**
 * Created by hoaipc on 10/27/15.
 */
public interface ResponseListener<T>
{
    void onResponse(T response);
    void onError();
}
