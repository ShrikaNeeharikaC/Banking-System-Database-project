package com.minibanking.view;

import com.minibanking.dao.*;
import com.minibanking.model.Account;
import com.minibanking.model.Customer;
import com.minibanking.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MainViewController {

    // --- DAO Instances ---
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();
    private final TransactionDao transactionDao = new TransactionDaoImpl();

    // --- Customer Tab UI Elements ---
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Long> customerIdColumn;
    @FXML private TableColumn<Customer, String> firstNameColumn;
    @FXML private TableColumn<Customer, String> lastNameColumn;

    // --- Account Tab UI Elements ---
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialBalanceField;
    @FXML private TableView<Account> accountTable;
    @FXML private TableColumn<Account, Long> accountIdColumn;
    @FXML private TableColumn<Account, String> accountNumberColumn;
    @FXML private TableColumn<Account, String> accountTypeColumn;
    @FXML private TableColumn<Account, BigDecimal> balanceColumn;

    // --- Transaction Tab UI Elements ---
    @FXML private ComboBox<Account> depositAccountComboBox;
    @FXML private TextField depositAmountField;
    @FXML private ComboBox<Account> withdrawAccountComboBox;
    @FXML private TextField withdrawAmountField;
    @FXML private ComboBox<Account> transferFromComboBox;
    @FXML private ComboBox<Account> transferToComboBox;
    @FXML private TextField transferAmountField;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Long> transactionIdColumn;
    @FXML private TableColumn<Transaction, String> transactionTypeColumn;
    @FXML private TableColumn<Transaction, BigDecimal> transactionAmountColumn;
    @FXML private TableColumn<Transaction, LocalDateTime> transactionDateColumn;
    @FXML private TableColumn<Transaction, String> fromAccountColumn;
    @FXML private TableColumn<Transaction, String> toAccountColumn;


    @FXML
    public void initialize() {
        // --- Initialize Customer Tab ---
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        // --- Initialize Account Tab ---
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountTypeComboBox.setItems(FXCollections.observableArrayList("Savings", "Checking"));


        // --- Initialize Transaction Tab ---
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        // Custom cell value factories to handle displaying account numbers from Account objects
        fromAccountColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            if (transaction.getFromAccount() != null) {
                return new SimpleStringProperty(transaction.getFromAccount().getAccountNumber());
            }
            return new SimpleStringProperty("N/A");
        });

        toAccountColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            if (transaction.getToAccount() != null) {
                return new SimpleStringProperty(transaction.getToAccount().getAccountNumber());
            }
            return new SimpleStringProperty("N/A");
        });

        // --- Load initial data ---
        refreshCustomerTable();
        refreshAccountTable();
        refreshTransactionTable();
    }

    // --- Customer Logic ---
    @FXML
    private void handleAddCustomer() {
        Customer customer = new Customer(firstNameField.getText(), lastNameField.getText(), addressField.getText(), phoneField.getText());
        customerDao.save(customer);
        refreshCustomerTable();
        clearCustomerFields();
    }

    // --- Account Logic ---
    @FXML
    private void handleAddAccount() {
        Customer selectedCustomer = customerComboBox.getValue();
        String accountType = accountTypeComboBox.getValue();
        BigDecimal balance = new BigDecimal(initialBalanceField.getText());

        if (selectedCustomer != null && accountType != null) {
            Account account = new Account(selectedCustomer, accountType, balance);
            accountDao.save(account);
            refreshAccountTable();
            clearAccountFields();
        }
    }

    // --- Transaction Logic ---
    @FXML
    private void handleDeposit() {
        Account toAccount = depositAccountComboBox.getValue();
        BigDecimal amount = new BigDecimal(depositAmountField.getText());

        if (toAccount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            Transaction transaction = new Transaction("DEPOSIT", amount, null, toAccount);
            transactionDao.save(transaction);

            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountDao.update(toAccount);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Deposit successful.");
            refreshAccountTable();
            refreshTransactionTable();
            clearTransactionFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid deposit details.");
        }
    }

    @FXML
    private void handleWithdraw() {
        Account fromAccount = withdrawAccountComboBox.getValue();
        BigDecimal amount = new BigDecimal(withdrawAmountField.getText());

        if (fromAccount != null && amount.compareTo(BigDecimal.ZERO) > 0 && fromAccount.getBalance().compareTo(amount) >= 0) {
            Transaction transaction = new Transaction("WITHDRAW", amount, fromAccount, null);
            transactionDao.save(transaction);

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountDao.update(fromAccount);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful.");
            refreshAccountTable();
            refreshTransactionTable();
            clearTransactionFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid withdrawal details or insufficient funds.");
        }
    }

    @FXML
    private void handleTransfer() {
        Account fromAccount = transferFromComboBox.getValue();
        Account toAccount = transferToComboBox.getValue();
        BigDecimal amount = new BigDecimal(transferAmountField.getText());

        if (fromAccount != null && toAccount != null && amount.compareTo(BigDecimal.ZERO) > 0 && fromAccount.getBalance().compareTo(amount) >= 0) {
            Transaction transaction = new Transaction("TRANSFER", amount, fromAccount, toAccount);
            transactionDao.save(transaction);

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountDao.update(fromAccount);
            accountDao.update(toAccount);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Transfer successful.");
            refreshAccountTable();
            refreshTransactionTable();
            clearTransactionFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid transfer details or insufficient funds.");
        }
    }

    // --- Helper Methods ---
    private void refreshCustomerTable() {
        List<Customer> customers = customerDao.findAll();
        customerTable.setItems(FXCollections.observableArrayList(customers));
        customerComboBox.setItems(FXCollections.observableArrayList(customers));
    }

    private void refreshAccountTable() {
        List<Account> accounts = accountDao.findAll();
        accountTable.setItems(FXCollections.observableArrayList(accounts));
        depositAccountComboBox.setItems(FXCollections.observableArrayList(accounts));
        withdrawAccountComboBox.setItems(FXCollections.observableArrayList(accounts));
        transferFromComboBox.setItems(FXCollections.observableArrayList(accounts));
        transferToComboBox.setItems(FXCollections.observableArrayList(accounts));
    }

    private void refreshTransactionTable() {
        List<Transaction> transactions = transactionDao.findAll();
        transactionTable.setItems(FXCollections.observableArrayList(transactions));
    }

    private void clearCustomerFields() {
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        phoneField.clear();
    }

    private void clearAccountFields() {
        customerComboBox.getSelectionModel().clearSelection();
        accountTypeComboBox.getSelectionModel().clearSelection();
        initialBalanceField.clear();
    }

    private void clearTransactionFields() {
        depositAccountComboBox.getSelectionModel().clearSelection();
        depositAmountField.clear();
        withdrawAccountComboBox.getSelectionModel().clearSelection();
        withdrawAmountField.clear();
        transferFromComboBox.getSelectionModel().clearSelection();
        transferToComboBox.getSelectionModel().clearSelection();
        transferAmountField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

