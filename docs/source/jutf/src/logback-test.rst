logback message Test
--------------------

::

    public class LogbackCapturingAppenderTest {
        @After
        public void cleanUp() {
            LogbackCapturingAppender.Factory.cleanUp();
        }

        @Test
        public void shouldCaptureAGivenLog() throws Exception {
            // Given
            LogbackCapturingAppender capturing = LogbackCapturingAppender.Factory.weaveInto(OurDomainWithLogger.LOG);

            // when
            OurDomainWithLogger domainClass = new OurDomainWithLogger();
            domainClass.logThis("This should be logged");

            // then
            assertThat(capturing.getCapturedLogMessage(), is("This should be logged"));
        }

        @Test
        public void shouldNotCaptureAGiveLogAfterCleanUp() throws Exception {

            // Given
            LogbackCapturingAppender capturing = LogbackCapturingAppender.Factory.weaveInto(OurDomainWithLogger.LOG);

            // when
            OurDomainWithLogger domainClass = new OurDomainWithLogger();
            domainClass.logThis("This should be logged at info");
            LogbackCapturingAppender.Factory.cleanUp();
            domainClass.logThis("This should not be logged");

            // then
            assertThat(capturing.getCapturedLogMessage(), is("This should be logged at info"));
        }
    }
