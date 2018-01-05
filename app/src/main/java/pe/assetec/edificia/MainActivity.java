package pe.assetec.edificia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import pe.assetec.edificia.fragment.BookingCreateFragment;
import pe.assetec.edificia.fragment.BookingFragment;
import pe.assetec.edificia.fragment.BookingListFragment;
import pe.assetec.edificia.fragment.CommentsListFragment;
import pe.assetec.edificia.fragment.DefaultFragment;
import pe.assetec.edificia.fragment.InvoiceDetailFragment;
import pe.assetec.edificia.fragment.InvoicesListFragment;
import pe.assetec.edificia.fragment.ReceiptFragment;
import pe.assetec.edificia.fragment.ReportFragment;
import pe.assetec.edificia.fragment.ShowPDFFragment;
import pe.assetec.edificia.fragment.ShowPdfReportFragment;
import pe.assetec.edificia.fragment.TicketFormFragment;
import pe.assetec.edificia.fragment.TicketFragment;
import pe.assetec.edificia.fragment.TicketsListFragment;
import pe.assetec.edificia.util.ManageSession;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ReceiptFragment.OnFragmentInteractionListener,ReportFragment.OnFragmentInteractionListener,
        BookingFragment.OnFragmentInteractionListener, TicketFragment.OnFragmentInteractionListener, InvoicesListFragment.OnFragmentInteractionListener , InvoiceDetailFragment.OnFragmentInteractionListener,
        TicketsListFragment.OnFragmentInteractionListener, CommentsListFragment.OnFragmentInteractionListener,
        TicketFormFragment.OnFragmentInteractionListener, ShowPDFFragment.OnFragmentInteractionListener, ShowPdfReportFragment.OnFragmentInteractionListener, DefaultFragment.OnFragmentInteractionListener, BookingListFragment.OnFragmentInteractionListener, BookingCreateFragment.OnFragmentInteractionListener {
    ManageSession session;


    private ImageView imageViewUser;
    private TextView textViewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Session
        session=new ManageSession(MainActivity.this);



        Fragment fragment = null;
        fragment=  new DefaultFragment();
        getFragmentManager().beginTransaction().replace(R.id.Contendor,fragment).addToBackStack(null).commit();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        textViewUser = (TextView)hView.findViewById(R.id.textViewUserName);
        textViewUser.setText(session.getUserName());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Boolean seleccionado = false;
        Boolean logOut = false;
        if (id == R.id.nav_receipt) {

            fragment = new ReceiptFragment();
            seleccionado = true;
            // Handle the camera action
        } else if (id == R.id.nav_report) {
            fragment = new ReportFragment();
            seleccionado = true;
        } else if (id == R.id.nav_booking) {
            fragment = new BookingFragment();
            seleccionado = true;
        } else if (id == R.id.nav_ticket) {
            fragment = new TicketFragment();
            seleccionado = true;
        }else if (id == R.id.nav_log_out) {

            logOut = true;
        }

        if (logOut){
            session.logOutUser();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (seleccionado){
            getFragmentManager().beginTransaction().replace(R.id.Contendor,fragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
