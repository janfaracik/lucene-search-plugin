package org.jenkinsci.plugins.lucene.search.databackend;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import org.jenkinsci.plugins.lucene.search.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

public class LuceneSearchBackendTest {
  @Rule public JenkinsRule rule = new JenkinsRule();
  private ExecutorService backgroundWorker;
  private JenkinsSearchBackend jenkinsSearchBackend;

  @Test
  public void assertAllFieldsAreMapped() {
    for (Field f : Field.values()) {
      assertTrue("Field: " + f + " not found", LuceneSearchBackend.FIELD_TYPE_MAP.containsKey(f));
    }
  }

  @Before
  public void setup() {
    backgroundWorker = Executors.newFixedThreadPool(1);
    jenkinsSearchBackend = new JenkinsSearchBackend(rule, backgroundWorker);
  }

  @After
  public void tearDown() {
    backgroundWorker.shutdownNow();
  }

  @Test(timeout = 10000)
  public void givenLuceneWhenJobsWithBuildsAreExecutedThenTheyShouldBeSearchable()
      throws IOException, ExecutionException, InterruptedException, SAXException,
          URISyntaxException, TimeoutException {
    jenkinsSearchBackend.setLuceneBackend(false);
    CommonTestCases.givenSearchWhenJobsWithBuildsAreExecutedThenTheyShouldBeSearchable(
        jenkinsSearchBackend, rule);
  }

  @Test(timeout = 10000)
  public void givenLuceneWhenIsNewItShouldSupportRebuildFromClean()
      throws IOException, ExecutionException, InterruptedException, SAXException,
          URISyntaxException {
    jenkinsSearchBackend.setLuceneBackend(false);
    CommonTestCases.givenSearchWhenIsNewItShouldSupportRebuildFromClean(jenkinsSearchBackend, rule);
  }
}
