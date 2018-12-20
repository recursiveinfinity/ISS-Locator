package com.example.isslocator;

import android.app.assist.AssistStructure;
import android.util.Pair;

import com.example.isslocator.common.LocationManager;
import com.example.isslocator.common.PermissionsManager;
import com.example.isslocator.dto.LocatorResponse;
import com.example.isslocator.net.LocatorService;
import com.example.isslocator.ui.home.HomeContract;
import com.example.isslocator.ui.home.HomePresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import pub.devrel.easypermissions.EasyPermissions;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EasyPermissions.class)

public class HomePresenterTest {

    @Mock
    private LocationManager locationManager;

    @Mock
    private PermissionsManager permissionsManager;

    @Mock
    private LocatorService locatorService;

    @Mock
    private HomeContract.View view;

    @Captor
    private ArgumentCaptor<List<Integer>> listArgumentCaptor;


    private HomePresenter homePresenter;

    private Pair<Double, Double> pair = new Pair<>(2.0, 4.0);


    @BeforeClass
    public static void setupRxSchedulers() {
        final Scheduler scheduler = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };
        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return scheduler;
            }
        });

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> scheduler);
        }

        @Before
        public void setup() {

            //PowerMockito.mockStatic(EasyPermissions.class);






            LocatorResponse locatorResponse = new LocatorResponse();

            when(locationManager.getLocationObservable())
                    .thenReturn(Observable.just(pair));

            when(locatorService.getISSPasses(pair.first, pair.second))
                    .thenReturn(Single.just(locatorResponse));


        }

        @Test
        public void testGetLocation_PermissionGranted() {
            //Given
            when(EasyPermissions.hasPermissions(any(), any()))
                    .thenReturn(true);
            when(permissionsManager.getPermissionGrantObservable())
                    .thenReturn(Observable.just(true));

            homePresenter = new HomePresenter(permissionsManager, locationManager,
                    locatorService, view);
            //When
            homePresenter.getLocation();


            //Then
            verify(permissionsManager).getPermissionGrantObservable();
            verify(locationManager).getLocationObservable();
            verify(locationManager).getLastKnownLocation();

            InOrder inOrder = inOrder(view);
            inOrder.verify(view).showProgress();
            inOrder.verify(view).hideProgress();
            verify(view).showResults(anyList());

        }

        @Test
        public void testGetLocation_PermissionNotGranted() {

        }

        @Test
        public void testGetLocation_APIFailure() {
            //Given
           /* when(EasyPermissions.hasPermissions(any(), any()))
                    .thenReturn(true);*/
            when(permissionsManager.getPermissionGrantObservable())
                    .thenReturn(Observable.just(true));
            when(locatorService.getISSPasses(pair.first, pair.second))
                    .thenReturn(Single.error(new IllegalAccessException("Invalid access")));

            homePresenter = new HomePresenter(permissionsManager, locationManager,
                    locatorService, view);
            //When
            homePresenter.getLocation();

            //Then
            view.showError("Invalid access");
        }

        @Test
        public void test() {
            when(EasyPermissions.hasPermissions(any(), any()))
                    .thenReturn(true);
            when(permissionsManager.getPermissionGrantObservable())
                    .thenReturn(Observable.just(true));
            when(locatorService.getISSPasses(pair.first, pair.second))
                    .thenReturn(Single.error(new IllegalAccessException("Invalid access")));

            homePresenter = new HomePresenter(permissionsManager, locationManager,
                    locatorService, view);

            homePresenter.testList();

            verify(view).showList(listArgumentCaptor.capture());

            List<Integer> result = listArgumentCaptor.getValue();

            Assert.assertEquals(10, result.size());

        }




}
