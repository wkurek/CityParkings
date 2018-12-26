package model.Card;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;

public class Card {
    private IntegerProperty cardId;
    private SimpleObjectProperty<Date> expirationDate;

    Card() {
        cardId = new SimpleIntegerProperty();
        expirationDate = new SimpleObjectProperty<>();
    }

    public int getCardId() {
        return cardId.get();
    }

    public IntegerProperty cardIdProperty() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId.set(cardId);
    }

    public Date getExpirationDate() {
        return expirationDate.get();
    }

    public SimpleObjectProperty<Date> expirationDateProperty() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate.set(expirationDate);
    }
}
