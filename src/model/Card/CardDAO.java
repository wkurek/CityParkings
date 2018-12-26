package model.Card;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CardDAO {
    private static class CardContract {
        static final String TABLE_NAME = "cards";
        static final String ID_COLUMN_NAME = "card_id";
        static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";
    }

    private static Card generateCard(CachedRowSet resultSet) throws SQLException {
        Card card = new Card();
        card.setCardId(resultSet.getInt(CardContract.ID_COLUMN_NAME));
        card.setExpirationDate(resultSet.getDate(CardContract.EXPIRATION_DATE_COLUMN_NAME));

        return card;
    }

    private static ObservableList<Card> generateCardList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Card> cardsList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Card card = generateCard(resultSet);
            cardsList.add(card);
        }

        return cardsList;
    }

    public static ObservableList<Card> getCards() {
        String sql = String.format("SELECT * FROM %s", CardContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Card> cardsList = null;

        try {
            cardsList = generateCardList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cardsList;
    }

    public static Card getCard(int id) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", CardContract.TABLE_NAME,
                CardContract.ID_COLUMN_NAME, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Card card = null;

        try {
            card = generateCard(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return card;
    }

    public static void saveCard(Card card) {
        
    }

    public static void updateCard(int id, Card updatedCard) {

    }

    public static void deleteCard(int id) {

    }

}
