package developer.sigmamovil.sigmatrackalpha1.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import developer.sigmamovil.sigmatrackalpha1.MainActivity;
import developer.sigmamovil.sigmatrackalpha1.domain.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import developer.sigmamovil.sigmatrackalpha1.ui.adapter.VisitHistoryAdapter;
import developer.sigmamovil.sigmatrackalpha1.io.model.VisitHistoryResponse;
import developer.sigmamovil.sigmatrackalpha1.R;
import developer.sigmamovil.sigmatrackalpha1.io.SigmaTrackApiAdapter;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitHistory;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class VisitHistoryFragment extends Fragment implements Callback<VisitHistoryResponse> {
    private RecyclerView mVisitHistoryList;
    private VisitHistoryAdapter adapter;
    public User user;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable("user");
        adapter = new VisitHistoryAdapter(getActivity(), user);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_visit_history, container, false);
        mVisitHistoryList = (RecyclerView) root.findViewById(R.id.visit_history_list);

        setupVisitHistoryList();
        //setDummyContent();

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        SigmaTrackApiAdapter.getApiServiceForVisitHistory()
                .getVisitHistory(user.getIdUser(), this);
    }

    private void setupVisitHistoryList() {
        mVisitHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVisitHistoryList.setAdapter(adapter);
    }

    private void setDummyContent() {
        ArrayList<VisitHistory> history = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            // history.add(new VisitHistory("Visita " + i, "Cliente " + i, "14/Sep/2015 14:01", "14/Sep/2015 16:10", "Here"));
        }

        adapter.addAll(history);
    }

    @Override
    public void success(VisitHistoryResponse visitHistoryResponse, Response response) {
        adapter.addAll(visitHistoryResponse.getHistory());
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }
}
