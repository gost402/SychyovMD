package com.example.dsychyov.sychyovmd.instrumented;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import com.example.dsychyov.sychyovmd.dao.DesktopAppDao;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDb;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DeleteDesktopAppByIdDaoTests {
    private DesktopAppDb desktopAppDb;
    private DesktopAppDao desktopAppDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

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

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[] { DesktopApp.Type.APPLICATION, 0 },
                new Object[] { DesktopApp.Type.URI, 0 },
                new Object[] { DesktopApp.Type.CONTACT, 0 }
        );
    }

    private final DesktopApp.Type mInput;
    private final int mOutput;

    public DeleteDesktopAppByIdDaoTests(DesktopApp.Type input, int output) {
        mInput = input;
        mOutput = output;
    }

    @Test
    public void deleteDesktopAppById() {
        DesktopApp desktopApp = new DesktopApp();
        desktopApp.name = "Name";
        desktopApp.value = "Value";
        desktopApp.type = mInput;

        assertEquals(0, desktopAppDao.loadAll().size());

        desktopAppDao.insertOnLastPosition(desktopApp);
        List<DesktopApp> desktopApps = desktopAppDao.loadAll();
        assertEquals(1, desktopApps.size());

        DesktopApp insertedDesktopApp = desktopApps.get(0);
        desktopAppDao.deleteDesktopAppByIdAndUpdatePositions(insertedDesktopApp.id);
        desktopApps = desktopAppDao.loadAll();
        assertEquals(mOutput, desktopApps.size());
    }
}
