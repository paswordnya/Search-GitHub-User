package com.github.user.searchgithub.view.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.user.searchgithub.App;
import com.github.user.searchgithub.R;
import com.github.user.searchgithub.model.UserResponseAPI;
import com.github.user.searchgithub.model.Users;
import com.github.user.searchgithub.network.helper.InternetConnection;
import com.github.user.searchgithub.presenter.SearchPresenter;
import com.github.user.searchgithub.view.detail.DetailUserGithub;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity implements SearchPresenter.View {

    private Toolbar toolbar, searchtollbar;
    private Menu search_menu;
    private MenuItem item_search;
    private SearchPresenter presenter;
    private RecyclerView recycleView;
    private UsersAdapter adapter;
    private ProgressBar progresbar;
    private LinearLayout LayoutNotfound;
    private NestedScrollView nestedScroolView;
    private int paginantion = 0;
    private String querys = "";
    private TextView errorTitle;
    private List<Users> adapterList = new ArrayList<>();
    private int totalCount;
    private CoordinatorLayout coordinatorLayout;
    private ImageView defaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSearchtollbar();
        recycleView = findViewById(R.id.recycleView);
        LayoutNotfound = findViewById(R.id.LayoutNotfound);
        progresbar = findViewById(R.id.progresBar);
        nestedScroolView = findViewById(R.id.nestedScroolView);
        errorTitle = findViewById(R.id.errorTitle);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        defaultImage = findViewById(R.id.defaultImage);
        presenter = new SearchPresenter(this);
        defaultImage.setVisibility(View.VISIBLE);

        if (nestedScroolView != null) {
            nestedScroolView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    App.getInstance().hideKeyboard(SearchUserActivity.this);
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        paginantion++;
                        if (totalCount == 403) {
                            return;
                        }
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            presenter.searchData(querys, paginantion);
                        } else {
                            Toast.makeText(SearchUserActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(this, adapterList);
        recycleView.setAdapter(adapter);
        recycleView.setNestedScrollingEnabled(false);
        adapter.setClickListener(new UsersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, String username) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent intDetail = new Intent(SearchUserActivity.this, DetailUserGithub.class);
                    intDetail.putExtra("username", username);
                    startActivity(intDetail);
                } else {
                    Toast.makeText(SearchUserActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, true);
                else
                    searchtollbar.setVisibility(View.VISIBLE);
                item_search.expandActionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSearchtollbar() {
        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    else
                        searchtollbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        }
    }

    public void initSearchView() {
        final SearchView searchView = (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        searchView.setSubmitButtonEnabled(false);
        LinearLayout linearLayoutOfSearchView = (LinearLayout) searchView.getChildAt(0);
        linearLayoutOfSearchView.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(getApplicationContext()); // and do whatever to your button
        img.setImageResource(R.drawable.ic_search);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 37, 0);
        linearLayoutOfSearchView.addView(img, layoutParams);
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search Github user..");
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(getResources().getColor(R.color.active_icon_color));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    try {
                        if (searchView.getQuery().toString().length() != 0) {
                            paginantion = 0;
                            adapterList.clear();
                            adapter.notifyDataSetChanged();
                            String encodedUrl = URLEncoder.encode(searchView.getQuery().toString(), "utf-8");
                            querys = encodedUrl;
                            presenter.searchData(querys, paginantion);
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Please enter a search word", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SearchUserActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    try {
                        if (query.length() != 0) {
                            paginantion = 0;
                            adapterList.clear();
                            adapter.notifyDataSetChanged();
                            String encodedUrl = URLEncoder.encode(query, "utf-8");
                            querys = encodedUrl;
                            presenter.searchData(querys, paginantion);
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Please enter a search word", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SearchUserActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                if (!query.trim().equalsIgnoreCase("")) {
                    //Do something
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        anim.start();


    }


    @Override
    public void onSuccess(Response<UserResponseAPI> response) {
        if (response.body().getItems().isEmpty()) {
            LayoutNotfound.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.GONE);
            return;
        }
        LayoutNotfound.setVisibility(View.GONE);
        recycleView.setVisibility(View.VISIBLE);
        adapterList.addAll(response.body().getItems());
        adapter.notifyDataSetChanged();
        defaultImage.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progresbar.setVisibility(View.VISIBLE);
        defaultImage.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progresbar.setVisibility(View.GONE);
        defaultImage.setVisibility(View.GONE);
    }

    @Override
    public void onFailure() {
        progresbar.setVisibility(View.GONE);
        defaultImage.setVisibility(View.GONE);
    }

    @Override
    public void errorCode(int code) {
        totalCount = 403;
        defaultImage.setVisibility(View.GONE);
        errorTitle.setText("Error code " + code);
        progresbar.setVisibility(View.GONE);
        recycleView.setVisibility(View.GONE);
        LayoutNotfound.setVisibility(View.VISIBLE);
    }


}