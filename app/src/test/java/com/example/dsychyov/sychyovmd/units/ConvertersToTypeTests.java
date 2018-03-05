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
public class ConvertersToTypeTests {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[] { 0, DesktopApp.Type.APPLICATION },
                new Object[] { 1, DesktopApp.Type.URI },
                new Object[] { 2, DesktopApp.Type.CONTACT },
                new Object[] { 3, null }
        );
    }

    private final int mInput;
    private final DesktopApp.Type mOutput;

    public ConvertersToTypeTests(Integer input, DesktopApp.Type output) {
        mInput = input;
        mOutput = output;
    }

    @Test
    public void convertToType() throws Exception {
        assertThat(mOutput, is(equalTo(Converters.toType(mInput))));
    }
}
