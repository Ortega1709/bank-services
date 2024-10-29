package com.ortega.notification.email;

import lombok.Getter;

public enum EmailTemplate {

    CREATION_ACCOUNT_NOTIFICATION("creation_account_notification.html", "Création du compte"),
    ACTIVATION_ACCOUNT_NOTIFICATION("activation_account_notification.html", "Activation du compte"),
    DEACTIVATION_ACCOUNT_NOTIFICATION("deactivation_account_notification.html", "Désactivation du compte");

    @Getter
    private final String template;

    @Getter
    private final String subject;

    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }

}
