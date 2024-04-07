package com.company.multidbmt.view.main;

import com.company.multidbmt.entity.User;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private CurrentAuthentication currentAuthentication;
    @ViewComponent
    private Span tenantBadge;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (currentAuthentication.isSet()
                && currentAuthentication.getUser() instanceof User user
                && user.getTenant() != null) {
            tenantBadge.setText(user.getTenant().getName());
        }
    }
}
