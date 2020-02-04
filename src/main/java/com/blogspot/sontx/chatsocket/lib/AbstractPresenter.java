package com.blogspot.sontx.chatsocket.lib;

import com.blogspot.sontx.chatsocket.lib.event.AppShutdownEvent;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public abstract class AbstractPresenter<TView extends BaseView> extends AbstractService implements Presenter {
    protected final TView view;

    protected AbstractPresenter(TView view) {
        this.view = view;
        wireUpViewEvents();
    }

    protected void wireUpViewEvents() {
        view.setOnClosingListener(this::onUserClosesView);
    }

    protected void onUserClosesView() {
        if (view.isMainWindow())
            post(new AppShutdownEvent());
    }
}
