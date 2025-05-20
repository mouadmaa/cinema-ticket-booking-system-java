package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private TableView<UserModel> userTableView;
    
    @Mock
    private TableColumn<UserModel, Integer> idColumn;
    
    @Mock
    private TableColumn<UserModel, String> roleColumn;
    
    @Mock
    private TableColumn<UserModel, String> firstNameColumn;
    
    @Mock
    private TableColumn<UserModel, String> lastNameColumn;
    
    @Mock
    private TableColumn<UserModel, String> usernameColumn;
    
    @Mock
    private TableColumn<UserModel, String> emailColumn;
    
    @Mock
    private TableColumn<UserModel, String> phoneNumberColumn;
    
    @Mock
    private TableColumn<UserModel, Void> actionsColumn;
    
    @Mock
    private Button addUserButton;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Spy
    @InjectMocks
    private UserController userController;
    
    private List<UserModel> testUsers;

    /**
     * Initialize JavaFX toolkit before all tests
     */
    @BeforeAll
    public static void initJavaFX() {
        // Skip JavaFX toolkit initialization in headless test environment
        try {
            // This approach allows tests to run in CI environment without a display
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
        } catch (Exception e) {
            System.out.println("Note: JavaFX initialization properties could not be set: " + e.getMessage());
        }
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Set up test users
        testUsers = new ArrayList<>();
        testUsers.add(new UserModel(1, "ADMIN", "Mohamed", "El Alaoui", "malaoui", "mohamed.elalaoui@gmail.com", "0661234567", "password123"));
        testUsers.add(new UserModel(2, "USER", "Fatima", "Benali", "fbenali", "fatima.benali@gmail.com", "0777654321", "securePass456"));
        testUsers.add(new UserModel(3, "MANAGER", "Youssef", "Bouazizi", "ybouazizi", "youssef.bouazizi@gmail.com", "0612345678", "youssef123"));
        
        // Mock UI interactions to avoid JavaFX thread issues
        doNothing().when(userController).initialize(any(URL.class), any(ResourceBundle.class));
        doNothing().when(userController).showErrorAlert(anyString(), anyString(), anyString());
        
        // Mock static SingletonConnection calls
        try (MockedStatic<SingletonConnection> mockedStatic = Mockito.mockStatic(SingletonConnection.class)) {
            mockedStatic.when(SingletonConnection::getConnection).thenReturn(mockConnection);
        }
        
        // Mock database operations
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }
    
    @Test
    public void testLoadUsers() throws SQLException {
        // Arrange
        try (MockedStatic<SingletonConnection> mockedStatic = Mockito.mockStatic(SingletonConnection.class)) {
            mockedStatic.when(SingletonConnection::getConnection).thenReturn(mockConnection);
            
            // Configure result set to return test users
            when(mockResultSet.next()).thenReturn(true, true, true, false); // 3 users then end
            when(mockResultSet.getInt("id")).thenReturn(1, 2, 3);
            when(mockResultSet.getString("role")).thenReturn("ADMIN", "USER", "MANAGER");
            when(mockResultSet.getString("first_name")).thenReturn("Mohamed", "Fatima", "Youssef");
            when(mockResultSet.getString("last_name")).thenReturn("El Alaoui", "Benali", "Bouazizi");
            when(mockResultSet.getString("username")).thenReturn("malaoui", "fbenali", "ybouazizi");
            when(mockResultSet.getString("email")).thenReturn("mohamed.elalaoui@gmail.com", "fatima.benali@gmail.com", "youssef.bouazizi@gmail.com");
            when(mockResultSet.getString("phone_number")).thenReturn("0661234567", "0777654321", "0612345678");
            
            // Mock UI interactions
            ObservableList<UserModel> usersList = spy(FXCollections.observableArrayList());
            when(userTableView.getItems()).thenReturn(usersList);
            doNothing().when(userController).autoResizeColumns();
            
            // Act
            userController.loadUsers();
            
            // Assert
            verify(mockConnection).prepareStatement(contains("SELECT id, role, first_name, last_name, username, email, phone_number FROM Users"));
            verify(mockPreparedStatement).executeQuery();
            verify(mockResultSet, times(4)).next();
            verify(userTableView).setItems(any());
        }
    }
    
    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        try (MockedStatic<SingletonConnection> mockedStatic = Mockito.mockStatic(SingletonConnection.class)) {
            mockedStatic.when(SingletonConnection::getConnection).thenReturn(mockConnection);
            
            int userId = 1;
            when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
            
            // Mock loadUsers to avoid JavaFX thread issues
            doNothing().when(userController).loadUsers();
            
            // Act
            userController.deleteUser(userId);
            
            // Assert
            verify(mockConnection).prepareStatement(eq("DELETE FROM Users WHERE id = ?"));
            verify(mockPreparedStatement).setInt(1, userId);
            verify(mockPreparedStatement).executeUpdate();
            verify(userController).loadUsers();
        }
    }
    
    @Test
    public void testDeleteUserNotFound() throws SQLException {
        // Arrange
        try (MockedStatic<SingletonConnection> mockedStatic = Mockito.mockStatic(SingletonConnection.class)) {
            mockedStatic.when(SingletonConnection::getConnection).thenReturn(mockConnection);
            
            int userId = 999; // Non-existent user
            when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
            
            // Act
            userController.deleteUser(userId);
            
            // Assert
            verify(mockConnection).prepareStatement(eq("DELETE FROM Users WHERE id = ?"));
            verify(mockPreparedStatement).setInt(1, userId);
            verify(mockPreparedStatement).executeUpdate();
            verify(userController).showErrorAlert(eq("Error"), eq("Delete Failed"), anyString());
            verify(userController, never()).loadUsers();
        }
    }
    
    @Test
    public void testDatabaseError() throws SQLException {
        // Arrange
        try (MockedStatic<SingletonConnection> mockedStatic = Mockito.mockStatic(SingletonConnection.class)) {
            mockedStatic.when(SingletonConnection::getConnection).thenReturn(mockConnection);
            
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database connection failed"));
            
            // Act
            userController.loadUsers();
            
            // Assert
            verify(userController).showErrorAlert(eq("Database Error"), eq("Could not connect to the database"), anyString());
        }
    }
    
    @Test
    public void testSetupTableColumns() {
        // Arrange - Mock the cell factory without needing to create the actual factory
        doNothing().when(actionsColumn).setCellFactory(any());
        
        // Skip the setupActionsColumn method which uses JavaFX components
        doNothing().when(userController).setupActionsColumn();
        
        // Act
        userController.setupTableColumns();
        
        // Assert
        verify(idColumn).setCellValueFactory(any());
        verify(roleColumn).setCellValueFactory(any());
        verify(firstNameColumn).setCellValueFactory(any());
        verify(lastNameColumn).setCellValueFactory(any());
        verify(usernameColumn).setCellValueFactory(any());
        verify(emailColumn).setCellValueFactory(any());
        verify(phoneNumberColumn).setCellValueFactory(any());
        
        verify(idColumn).setMaxWidth(anyDouble());
        verify(roleColumn).setMaxWidth(anyDouble());
        verify(firstNameColumn).setMaxWidth(anyDouble());
        verify(lastNameColumn).setMaxWidth(anyDouble());
        verify(usernameColumn).setMaxWidth(anyDouble());
        verify(emailColumn).setMaxWidth(anyDouble());
        verify(phoneNumberColumn).setMaxWidth(anyDouble());
        verify(actionsColumn).setMaxWidth(anyDouble());
    }
    
    @Test
    public void testHandleAddUserButton() {
        // Arrange
        doNothing().when(userController).openUserForm(isNull());
        
        // Act
        userController.handleAddUserButton(mock(javafx.event.ActionEvent.class));
        
        // Assert
        verify(userController).openUserForm(isNull());
    }
    
    @Test
    public void testHandleUpdateUser() {
        // Arrange
        UserModel testUser = testUsers.get(0);
        doNothing().when(userController).openUserForm(any(UserModel.class));
        
        // Act
        userController.handleUpdateUser(testUser);
        
        // Assert
        verify(userController).openUserForm(same(testUser));
    }
    
    @Test
    public void testAutoResizeColumns() {
        // Arrange
        when(userTableView.getColumns()).thenReturn(FXCollections.observableArrayList(
                idColumn, roleColumn, firstNameColumn, lastNameColumn, 
                usernameColumn, emailColumn, phoneNumberColumn, actionsColumn));
        
        when(idColumn.getWidth()).thenReturn(50.0);
        when(roleColumn.getWidth()).thenReturn(100.0);
        when(firstNameColumn.getWidth()).thenReturn(120.0);
        when(lastNameColumn.getWidth()).thenReturn(120.0);
        when(usernameColumn.getWidth()).thenReturn(120.0);
        when(emailColumn.getWidth()).thenReturn(180.0);
        when(phoneNumberColumn.getWidth()).thenReturn(120.0);
        when(actionsColumn.getWidth()).thenReturn(70.0);
        
        // Act
        userController.autoResizeColumns();
        
        // Assert
        verify(userTableView).setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        verify(idColumn).setPrefWidth(50.0);
        verify(roleColumn).setPrefWidth(100.0);
        verify(firstNameColumn).setPrefWidth(120.0);
        verify(lastNameColumn).setPrefWidth(120.0);
        verify(usernameColumn).setPrefWidth(120.0);
        verify(emailColumn).setPrefWidth(180.0);
        verify(phoneNumberColumn).setPrefWidth(120.0);
        verify(actionsColumn).setPrefWidth(70.0);
    }
}
