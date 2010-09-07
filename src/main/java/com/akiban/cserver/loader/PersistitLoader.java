package com.akiban.cserver.loader;

import com.akiban.cserver.store.PersistitStore;
import com.persistit.Transaction;
import com.persistit.exception.PersistitException;

import java.sql.ResultSet;
import java.util.List;

public class PersistitLoader
{
    public PersistitLoader(PersistitStore store, DB db, Tracker tracker)
            throws Exception
    {
        this.store = store;
        this.db = db;
        this.tracker = tracker;
        // this.transaction = store.getDb().getTransaction();
    }

    public void load(List<GenerateFinalTask> finalTasks) throws Exception
    {
        // transaction.begin();
        DB.Connection connection = db.new Connection();
        try {
            // TODO: Merge inputs from final tasks by hkey. This would require a
            // TODO: connection per table.
            for (GenerateFinalTask task : finalTasks) {
                load(task, connection);
            }
            // transaction.commit();
        } catch (PersistitException e) {
            tracker.error("Caught exception while loading persistit", e);
/*
            try {
                transaction.rollback();
            } catch (PersistitException rollbackException) {
                tracker.error("Caught exception while rolling back following earlier failure", rollbackException);
            }
            throw e;
*/
        } finally {
            // transaction.end();
        }
    }

    private void load(final GenerateFinalTask task, DB.Connection connection)
            throws Exception
    {
        tracker.info(String.format("Loading persistit for %s", task.artifactTableName()));
        final PersistitAdapter persistitAdapter = new PersistitAdapter(store, task);
        connection.new Query(SQL_TEMPLATE, task.artifactTableName())
        {
            @Override
            protected void handleRow(ResultSet resultSet) throws Exception
            {
                persistitAdapter.handleRow(resultSet);
                count++;
                if (count % LOG_INTERVAL == 0) {
                    tracker.info("%s: %s", task.artifactTableName(), count);
                }
            }

            private int count = 0;
        }.execute();
        persistitAdapter.close();
    }

    private static final String SQL_TEMPLATE = "select * from %s";
    private static final int LOG_INTERVAL = 10 * 1000;

    private final DB db;
    private final PersistitStore store;
    private final Tracker tracker;
    // private final Transaction transaction;
}
