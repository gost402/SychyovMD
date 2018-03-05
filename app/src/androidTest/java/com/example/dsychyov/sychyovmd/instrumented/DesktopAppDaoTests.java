package com.example.dsychyov.sychyovmd.instrumented;


import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.dsychyov.sychyovmd.dao.DesktopAppDao;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDb;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DesktopAppDaoTests {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DesktopAppDb desktopAppDb;
    private DesktopAppDao desktopAppDao;

    @Before
    public void initDb() throws Exception {
        desktopAppDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), DesktopAppDb.class)
                .allowMainThreadQueries()
                .build();

        desktopAppDao = desktopAppDb.desktopAppsDao();
    }

    @After
    public void closeDb() throws Exception {
        desktopAppDb.close();
    }

    @Test
    public void insertAssignCorrectPositions() {
        DesktopApp desktopApp = buildDesktopApp();

        assertEquals(0, desktopAppDao.loadAll().size());

        desktopAppDao.insertOnLastPosition(desktopApp);
        desktopAppDao.insertOnLastPosition(desktopApp);
        desktopAppDao.insertOnLastPosition(desktopApp);

        List<DesktopApp> desktopApps = desktopAppDao.loadAll();

        assertEquals(3, desktopApps.size());

        validateDesktopAppPositions(desktopApps);
    }

    @Test
    public void updatePositionsAfterDelete() {
        DesktopApp desktopApp = buildDesktopApp();

        assertEquals(0, desktopAppDao.loadAll().size());

        desktopAppDao.insertOnLastPosition(desktopApp);
        desktopAppDao.insertOnLastPosition(desktopApp);
        desktopAppDao.insertOnLastPosition(desktopApp);

        List<DesktopApp> desktopApps = desktopAppDao.loadAll();

        assertEquals(3, desktopApps.size());

        desktopAppDao.deleteDesktopAppByIdAndUpdatePositions(desktopApps.get(0).id);

        desktopApps = desktopAppDao.loadAll();
        assertEquals(2, desktopApps.size());

        validateDesktopAppPositions(desktopApps);
    }

    @Test
    public void swapPositions() {
        DesktopApp desktopApp = buildDesktopApp();

        assertEquals(0, desktopAppDao.loadAll().size());

        desktopAppDao.insertOnLastPosition(desktopApp);
        desktopAppDao.insertOnLastPosition(desktopApp);

        List<DesktopApp> desktopApps = desktopAppDao.loadAll();

        assertEquals(2, desktopApps.size());

        desktopAppDao.swapPositions(desktopApps.get(0).id, desktopApps.get(1).id);

        desktopApps = desktopAppDao.loadAll();

        assertEquals(1, desktopApps.get(0).position.intValue());
        assertEquals(0, desktopApps.get(1).position.intValue());
    }

    @Test
    public void updateIcon() {
        DesktopApp desktopApp = buildDesktopApp();
        byte[] icon = new byte[] { 1, 2, 3, 4 };

        assertEquals(0, desktopAppDao.loadAll().size());

        desktopAppDao.insertOnLastPosition(desktopApp);
        List<DesktopApp> desktopApps = desktopAppDao.loadAll();
        assertEquals(1, desktopApps.size());

        desktopAppDao.updateDesktopAppCustomIcon(desktopApps.get(0).id, icon);

        desktopApps = desktopAppDao.loadAll();
        DesktopApp iconDesktopApp = desktopApps.get(0);

        for(int i = 0; i < icon.length; ++i) {
            assertEquals(icon[i], iconDesktopApp.customIcon[i]);
        }
    }

    private void validateDesktopAppPositions(List<DesktopApp> desktopApps) {
        for(int i = 0; i < desktopApps.size(); ++i) {
            assertEquals(i, desktopApps.get(i).position.intValue());
        }
    }

    private DesktopApp buildDesktopApp() {
        DesktopApp desktopApp = new DesktopApp();
        desktopApp.name = "Name";
        desktopApp.value = "Value";
        desktopApp.type = DesktopApp.Type.APPLICATION;
        return desktopApp;
    }
}
