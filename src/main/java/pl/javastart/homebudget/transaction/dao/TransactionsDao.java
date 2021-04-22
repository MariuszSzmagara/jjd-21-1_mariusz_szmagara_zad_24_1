package pl.javastart.homebudget.transaction.dao;

import org.springframework.stereotype.Component;
import pl.javastart.homebudget.transaction.model.Transaction;
import pl.javastart.homebudget.transaction.model.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionsDao {
    private final Connection connection;

    public TransactionsDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_budget?useSSL=false", "root", "root");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void add(Transaction transaction) {
        final String sqlQuery = "INSERT INTO transaction(type, description, amount, data) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setDate(4, transaction.getDate());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean updateById(Transaction transaction) {
        final String sqlQuery = "UPDATE transaction SET type = ?, description = ?, amount = ?, data = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setDate(4, transaction.getDate());
            preparedStatement.setInt(5, transaction.getId());
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean deleteById(int id) {
        final String sqlQuery = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            return deletedRows != 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<Transaction> searchByType(Type transactionType) {
        final String sqlQuery = "SELECT id, type, description, amount, data FROM transaction WHERE type = ?";
        List<Transaction> chosenTransactionsTypeList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, transactionType.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Type type = Type.valueOf(resultSet.getString("type"));
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date data = resultSet.getDate("data");
                chosenTransactionsTypeList.add(new Transaction(id, type, description, amount, data));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return chosenTransactionsTypeList;
    }

    public List<Transaction> getAll() {
        final String sqlQuery = "SELECT * FROM transaction";
        List<Transaction> allTransactions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Type type = Type.valueOf(resultSet.getString("type"));
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date data = resultSet.getDate("data");
                allTransactions.add(new Transaction(id, type, description, amount, data));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return allTransactions;
    }

    public Optional<Transaction> getById(int requestId) {
        final String sqlQuery = "SELECT id, type, description, amount, data FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, requestId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                Type type = Type.valueOf(resultSet.getString("type"));
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date data = resultSet.getDate("data");
                return Optional.of(new Transaction(id, type, description, amount, data));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
