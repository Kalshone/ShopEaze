package com.example.shopeaze;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginContract.View view;
    @Mock
    LoginContract.Model model;

    @Test
    public void nullViewObject(){
        assertEquals(view.getEmail(), null);
    }
    @Test
    public void checkEmptyUsername(){
        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter email");
    }
    @Test
    public void checkEmptyPassword(){
        when(view.getEmail()).thenReturn("anyemail@email.com");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter password");
    }
    @Test
    public void emptyEmailOrder(){
        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        InOrder order = inOrder(view);
        order.verify(view).getEmail();
        order.verify(view).showErrorMessage("Please enter email");
    }

    @Test
    public void userFound(){
        when(view.getEmail()).thenReturn("shopper1@sample.com");
        when(view.getPassword()).thenReturn("123456");

        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();

        ArgumentCaptor<LoginModel.OnLoginFinishedListener> argumentCaptor = ArgumentCaptor.forClass(LoginModel.OnLoginFinishedListener.class);
        verify(model).loginUser(anyString(), anyString(), argumentCaptor.capture());
        LoginModel.OnLoginFinishedListener listener = argumentCaptor.getValue();
        listener.onLoginSuccess();

        verify(view).showProgressBar();
        verify(view).hideProgressBar();
        verify(view).showLoginSuccessMessage();
        verify(view).navigateToStoreList();
    }

    @Test
    public void userNotFound(){
        when(view.getEmail()).thenReturn("foobar@email.com");
        when(view.getPassword()).thenReturn("123456");

        ArgumentCaptor<LoginModel.OnLoginFinishedListener> argumentCaptor = ArgumentCaptor.forClass(LoginModel.OnLoginFinishedListener.class);

        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();

        verify(model).loginUser(anyString(), anyString(), argumentCaptor.capture());
        LoginModel.OnLoginFinishedListener listener = argumentCaptor.getValue();
        listener.onLoginFailure();
        verify(view).showProgressBar();
        verify(view).hideProgressBar();
        verify(view).showErrorMessage("Authentication failed");

    }

}