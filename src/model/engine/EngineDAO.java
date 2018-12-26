package model.engine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class EngineDAO {
    private static class EngineContract {
        static final String TABLE_NAME = "engines";
        static final String COLUMN_NAME_TYPE = "engine_type";
    }

    public static Engine generateEngine(CachedRowSet resultSet) throws SQLException {
        Engine engine = new Engine();
        engine.setType(resultSet.getString(EngineContract.COLUMN_NAME_TYPE));

        return engine;
    }

    private static ObservableList<Engine> generateEngineList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Engine> engineList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Engine engine = generateEngine(resultSet);
            engineList.add(engine);
        }

        return engineList;
    }

    public static ObservableList<Engine> getEngine() {
        String sql = String.format("SELECT * FROM %s", EngineContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Engine> engineList = null;

        try {
            engineList = generateEngineList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return engineList;
    }

    public static Engine getEngine(String engineType) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%s", EngineContract.TABLE_NAME,
                EngineContract.COLUMN_NAME_TYPE, engineType);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Engine engine = null;

        try {
            engine = generateEngine(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return engine;
    }

    public static void saveEngine(Engine engine) {
        if(engine == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES ('%s')", EngineContract.TABLE_NAME, engine.getType());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteEngine(String engineType) {
        String sql = String.format("DELETE FROM %s WHERE %s = %s", EngineContract.TABLE_NAME,
                EngineContract.COLUMN_NAME_TYPE, engineType);

        DbHelper.executeUpdateQuery(sql);
    }
}
