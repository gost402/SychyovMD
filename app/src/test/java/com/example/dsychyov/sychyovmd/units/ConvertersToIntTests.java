package com.example.dsychyov.sychyovmd.units;

import android.support.v4.util.Pair;

import com.example.dsychyov.sychyovmd.utils.Converters;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ConvertersToIntTests {
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[] { DesktopApp.Type.APPLICATION, 0 },
                new Object[] { DesktopApp.Type.URI, 1 },
                new Object[] { DesktopApp.Type.CONTACT, 2 }
        );
    }

    private final DesktopApp.Type mInput;
    private final int mOutput;

    public ConvertersToIntTests(DesktopApp.Type input, Integer output) {
        mInput = input;
        mOutput = output;
    }

    @Test
    public void convertToInt() throws Exception {
        assertThat(mOutput, is(equalTo(Converters.toInteger(mInput))));
    }
}
