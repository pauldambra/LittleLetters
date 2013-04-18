package org.sleepyparents.littleletters.test;

import org.robolectric.RobolectricTestRunner;
import org.sleepyparents.littleletters.MainActivity;
import org.sleepyparents.littleletters.R;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ProofIfProofBeNeedBeTests {
	@Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = new MainActivity().getResources()
        		.getString(R.string.hello_world);
        assertThat(hello, equalTo("Hello world!"));
    }
}
