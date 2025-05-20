package com.example.cinema_ticket_booking_system.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.models.UserModel;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private JFXButton homeButton;
    
    @FXML
    private JFXButton userButton;

    @FXML
    private JFXButton movieButton;
    
    @FXML
    private JFXButton hallButton;
    
    @FXML
    private JFXButton seatButton;

    @FXML
    private JFXButton showButton;
    
    @FXML
    private JFXButton clientButton;
    
    @FXML
    private JFXButton bookingButton;
    
    @FXML
    private JFXButton ticketButton;
    
    @FXML
    private JFXButton paymentButton;
    
    @FXML
    private AnchorPane contentPane;

    private UserModel currentUser;

    private final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #1E88E5; -fx-text-fill: white;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(3.0);
        shadow.setOffsetX(0.0);
        shadow.setColor(javafx.scene.paint.Color.color(0, 0, 0, 0.3));
        contentPane.setEffect(shadow);

        setupButtonIcons();

        try {
            loadHomeView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
            
    @FXML
    private void handleMovieButton() throws IOException {
        loadMovieView();
    }
    
    @FXML
    private void handleHallButton() throws IOException {
        loadHallView();
    }
    
    @FXML
    private void handleSeatButton() throws IOException {
        loadSeatView();
    }
    
    @FXML
    private void handleShowButton() throws IOException {
        loadShowView();
    }
    
    @FXML
    private void handleClientButton() throws IOException {
        loadClientView();
    }
    
    @FXML
    private void handleBookingButton() throws IOException {
        loadBookingView();
    }
    
    @FXML
    private void handleTicketButton() throws IOException {
        loadTicketView();
    }
    
    @FXML
    private void handlePaymentButton() throws IOException {
        loadPaymentView();
    }

    private void setupButtonIcons() {
        FontAwesomeIconView homeIcon = new FontAwesomeIconView(FontAwesomeIcon.HOME);
        homeIcon.setSize("18");
        homeIcon.setFill(Color.WHITE);
        homeButton.setGraphic(homeIcon);
        homeButton.setGraphicTextGap(10);

        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USERS);
        userIcon.setSize("18");
        userIcon.setFill(Color.WHITE);
        userButton.setGraphic(userIcon);
        userButton.setGraphicTextGap(10);

        FontAwesomeIconView movieIcon = new FontAwesomeIconView(FontAwesomeIcon.FILM);
        movieIcon.setSize("18");
        movieIcon.setFill(Color.WHITE);
        movieButton.setGraphic(movieIcon);
        movieButton.setGraphicTextGap(10);

        FontAwesomeIconView hallIcon = new FontAwesomeIconView(FontAwesomeIcon.BUILDING);
        hallIcon.setSize("18");
        hallIcon.setFill(Color.WHITE);
        hallButton.setGraphic(hallIcon);
        hallButton.setGraphicTextGap(10);

        FontAwesomeIconView seatIcon = new FontAwesomeIconView(FontAwesomeIcon.TICKET);
        seatIcon.setSize("18");
        seatIcon.setFill(Color.WHITE);
        seatButton.setGraphic(seatIcon);
        seatButton.setGraphicTextGap(10);

        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.CALENDAR);
        showIcon.setSize("18");
        showIcon.setFill(Color.WHITE);
        showButton.setGraphic(showIcon);
        showButton.setGraphicTextGap(10);

        FontAwesomeIconView clientIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
        clientIcon.setSize("18");
        clientIcon.setFill(Color.WHITE);
        clientButton.setGraphic(clientIcon);
        clientButton.setGraphicTextGap(10);

        FontAwesomeIconView bookingIcon = new FontAwesomeIconView(FontAwesomeIcon.TICKET);
        bookingIcon.setSize("18");
        bookingIcon.setFill(Color.WHITE);
        bookingButton.setGraphic(bookingIcon);
        bookingButton.setGraphicTextGap(10);

        FontAwesomeIconView ticketIcon = new FontAwesomeIconView(FontAwesomeIcon.CREDIT_CARD);
        ticketIcon.setSize("18");
        ticketIcon.setFill(Color.WHITE);
        ticketButton.setGraphic(ticketIcon);
        ticketButton.setGraphicTextGap(10);

        FontAwesomeIconView paymentIcon = new FontAwesomeIconView(FontAwesomeIcon.MONEY);
        paymentIcon.setSize("18");
        paymentIcon.setFill(Color.WHITE);
        paymentButton.setGraphic(paymentIcon);
        paymentButton.setGraphicTextGap(10);

        homeButton.getStyleClass().add("dashboard-button");
        userButton.getStyleClass().add("dashboard-button");
        movieButton.getStyleClass().add("dashboard-button");
        hallButton.getStyleClass().add("dashboard-button");
        seatButton.getStyleClass().add("dashboard-button");
        showButton.getStyleClass().add("dashboard-button");
        clientButton.getStyleClass().add("dashboard-button");
        bookingButton.getStyleClass().add("dashboard-button");
        ticketButton.getStyleClass().add("dashboard-button");
        paymentButton.getStyleClass().add("dashboard-button");
    }

    @FXML
    private void handleHomeButton() throws IOException {
        loadHomeView();
    }

    @FXML
    private void handleUserButton() throws IOException {
        loadUserView();
    }

    private void loadHomeView() throws IOException {
        resetButtonStyles();
        homeButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("HomeView.fxml"));
        AnchorPane homeView = fxmlLoader.load();
        setContent(homeView);
    }
    
    private void loadUserView() throws IOException {
        resetButtonStyles();
        userButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("UserView.fxml"));
        AnchorPane userView = fxmlLoader.load();
        setContent(userView);
    }
    
    private void loadMovieView() throws IOException {
        resetButtonStyles();
        movieButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("MovieView.fxml"));
        BorderPane movieView = fxmlLoader.load();
        setContent(movieView);
    }
    
    private void loadHallView() throws IOException {
        resetButtonStyles();
        hallButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("HallView.fxml"));
        BorderPane hallView = fxmlLoader.load();
        setContent(hallView);
    }
    
    private void loadSeatView() throws IOException {
        resetButtonStyles();
        seatButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("SeatView.fxml"));
        BorderPane seatView = fxmlLoader.load();
        setContent(seatView);
    }
    
    private void resetButtonStyles() {
        String DEFAULT_BUTTON_STYLE = "-fx-background-color: #263238; -fx-text-fill: white;";
        homeButton.setStyle(DEFAULT_BUTTON_STYLE);
        userButton.setStyle(DEFAULT_BUTTON_STYLE);
        movieButton.setStyle(DEFAULT_BUTTON_STYLE);
        hallButton.setStyle(DEFAULT_BUTTON_STYLE);
        seatButton.setStyle(DEFAULT_BUTTON_STYLE);
        showButton.setStyle(DEFAULT_BUTTON_STYLE);
        clientButton.setStyle(DEFAULT_BUTTON_STYLE);
        bookingButton.setStyle(DEFAULT_BUTTON_STYLE);
        ticketButton.setStyle(DEFAULT_BUTTON_STYLE);
        paymentButton.setStyle(DEFAULT_BUTTON_STYLE);
    }
    
    private void loadShowView() throws IOException {
        resetButtonStyles();
        showButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("ShowView.fxml"));
        BorderPane showView = loader.load();
        setContent(showView);
    }
    
    private void loadClientView() throws IOException {
        resetButtonStyles();
        clientButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("ClientView.fxml"));
        BorderPane clientView = loader.load();
        setContent(clientView);
    }
    
    private void loadBookingView() throws IOException {
        resetButtonStyles();
        bookingButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("BookingView.fxml"));
        BorderPane bookingView = loader.load();
        setContent(bookingView);
    }
    
    private void loadTicketView() throws IOException {
        resetButtonStyles();
        ticketButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("TicketView.fxml"));
        BorderPane ticketView = loader.load();
        setContent(ticketView);
    }
    
    private void loadPaymentView() throws IOException {
        resetButtonStyles();
        paymentButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("PaymentView.fxml"));
        BorderPane paymentView = loader.load();
        setContent(paymentView);
    }

    private void setContentPane(AnchorPane view) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2.0);
        shadow.setOffsetX(0.0);
        shadow.setColor(javafx.scene.paint.Color.color(0, 0, 0, 0.2));
        view.setEffect(shadow);

        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }

    private void setContent(javafx.scene.Parent view) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2.0);
        shadow.setOffsetX(0.0);
        shadow.setColor(javafx.scene.paint.Color.color(0, 0, 0, 0.2));
        view.setEffect(shadow);

        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }

    public void setCurrentUser(UserModel user) {
        this.currentUser = user;
    }
}
