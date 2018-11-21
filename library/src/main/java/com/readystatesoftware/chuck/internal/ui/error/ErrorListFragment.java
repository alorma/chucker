package com.readystatesoftware.chuck.internal.ui.error;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.readystatesoftware.chuck.R;
import com.readystatesoftware.chuck.internal.data.ChuckContentProvider;
import com.readystatesoftware.chuck.internal.data.LocalCupboard;
import com.readystatesoftware.chuck.internal.data.RecordedThrowable;
import com.readystatesoftware.chuck.internal.support.SQLiteUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.readystatesoftware.chuck.internal.data.ChuckContentProvider.LOADER_ERRORS;

/**
 * @author Olivier Perez
 */
public class ErrorListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String THROWABLES = "THROWABLES";
    private ErrorAdapter adapter;
    private ErrorAdapter.ErrorListListener listener;
    private List<RecordedThrowable> recordedThrowables;

    public static Fragment newInstance() {
        return new ErrorListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ErrorAdapter.ErrorListListener) {
            listener = (ErrorAdapter.ErrorListListener) context;
        } else {
            throw new IllegalArgumentException("Context must implement the listener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chuck_fragment_error_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            adapter = new ErrorAdapter(getContext(), listener);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            final RecordedThrowable[] throwables = (RecordedThrowable[]) savedInstanceState.getParcelableArray(THROWABLES);
            recordedThrowables = new ArrayList<>(Arrays.asList(throwables));
            adapter.submitList(recordedThrowables);
        } else {
            getLoaderManager().initLoader(LOADER_ERRORS, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chuck_errors_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            askForConfirmation();
            return true;
        } else if (item.getItemId() == R.id.browse_sql) {
            SQLiteUtils.browseDatabase(getContext());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(THROWABLES, recordedThrowables.toArray(new RecordedThrowable[]{}));
    }

    private void askForConfirmation() {
        new AlertDialog.Builder(getContext())
            .setTitle(R.string.chuck_clear)
            .setMessage(R.string.chuck_clear_error_confirmation)
            .setPositiveButton(R.string.chuck_clear, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getContext().getContentResolver().delete(ChuckContentProvider.ERROR_URI, null, null);
                }
            })
            .setNegativeButton(R.string.chuck_cancel, null)
            .show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = new CursorLoader(getContext());
        loader.setUri(ChuckContentProvider.ERROR_URI);
        loader.setProjection(RecordedThrowable.PARTIAL_PROJECTION);
        loader.setSortOrder("date DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        recordedThrowables = new ArrayList<>(LocalCupboard.getInstance().withCursor(data).list(RecordedThrowable.class));
        adapter.submitList(recordedThrowables);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}
