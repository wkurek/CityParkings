package model.card;

import javafx.collections.FXCollections;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CardDAO {
    public static class CardContract {
        public static final String TABLE_NAME = "[dbo].[cards]";
        public static final String COLUMN_NAME_ID = "card_id";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }

    public static model.card.Card generateCard(CachedRowSet resultSet) throws SQLException {
        model.card.Card card = new Card();
        card.setCardId(resultSet.getInt(CardContract.COLUMN_NAME_ID));
        card.setExpirationDate(resultSet.getDate(CardContract.COLUMN_NAME_EXPIRATION_DATE));

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
                CardContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Card card = null;

        try {
            card = generateCard(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return card;
    }

    public static Integer getSavedCardIndex() {
        String sql = String.format("SELECT IDENT_CURRENT ('%s') AS %s", CardContract.TABLE_NAME,
                CardContract.COLUMN_NAME_ID);
        CachedRowSet result = DbHelper.executeQuery(sql);

        Integer index = null;

        try {
            if(result.next()) {
                index = result.getInt(CardContract.COLUMN_NAME_ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    public static void saveCard(Card card) {
        if(card == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, '%s')", CardContract.TABLE_NAME, card.getExpirationDate());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateCard(int id, Card updatedCard) {
        if(updatedCard == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = %d", CardContract.TABLE_NAME,
                CardContract.COLUMN_NAME_EXPIRATION_DATE, updatedCard.getExpirationDate(), CardContract.COLUMN_NAME_ID,
                updatedCard.getCardId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteCard(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", CardContract.TABLE_NAME,
                CardContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

}
