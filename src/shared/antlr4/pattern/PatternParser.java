// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/pattern/Pattern.g4 by ANTLR 4.7.2
package shared.antlr4.pattern;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PatternParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, Operator=45, 
		NOT=46, AND=47, OR=48, Timeunit=49, Litterals=50, Skip=51;
	public static final int
		RULE_patternEntry = 0, RULE_temporalPattern = 1, RULE_graphProcessing = 2, 
		RULE_collectStreams = 3, RULE_streamProcessing = 4, RULE_computation = 5, 
		RULE_selection = 6, RULE_partition = 7, RULE_extraction = 8, RULE_evaluation = 9, 
		RULE_operation = 10, RULE_computationFunction = 11, RULE_selectionFunction = 12, 
		RULE_edgeSelection = 13, RULE_logicalExpression = 14, RULE_booleanAndExpression = 15, 
		RULE_unaryExpression = 16, RULE_primaryExpression = 17, RULE_boolPredicate = 18, 
		RULE_operands = 19, RULE_partitionFunction = 20, RULE_operationFunction = 21, 
		RULE_oneFieldOperationAlias = 22, RULE_triggerComputation = 23, RULE_triggerInput = 24, 
		RULE_triggerTemporal = 25, RULE_triggerSensitivity = 26, RULE_emission = 27, 
		RULE_temporalVariable = 28, RULE_functionName = 29, RULE_label = 30, RULE_value = 31, 
		RULE_variable = 32, RULE_fireEvent = 33, RULE_tupleField = 34;
	private static String[] makeRuleNames() {
		return new String[] {
			"patternEntry", "temporalPattern", "graphProcessing", "collectStreams", 
			"streamProcessing", "computation", "selection", "partition", "extraction", 
			"evaluation", "operation", "computationFunction", "selectionFunction", 
			"edgeSelection", "logicalExpression", "booleanAndExpression", "unaryExpression", 
			"primaryExpression", "boolPredicate", "operands", "partitionFunction", 
			"operationFunction", "oneFieldOperationAlias", "triggerComputation", 
			"triggerInput", "triggerTemporal", "triggerSensitivity", "emission", 
			"temporalVariable", "functionName", "label", "value", "variable", "fireEvent", 
			"tupleField"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'.g()'", "'.collect('", "','", "')'", "'.compute('", "'.select('", 
			"'.GroupV('", "'.GroupE('", "'.extractV('", "'.extractE('", "'.evaluate('", 
			"'.'", "', ['", "']'", "'EDGE['", "'('", "'map'", "'flatmap'", "'reduce'", 
			"'filter'", "'groupby'", "'Merge'", "'collect'", "'avg'", "'max'", "'min'", 
			"'count'", "'select'", "'.trigger('", "'edge'", "'vertex'", "'addition'", 
			"'deletion'", "'update'", "'as'", "'['", "'.emit('", "'ago'", "'every'", 
			"'within'", "'''", "'$'", "'\"'", null, "'not'", "'and'", "'or'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "Operator", "NOT", 
			"AND", "OR", "Timeunit", "Litterals", "Skip"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Pattern.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PatternParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class PatternEntryContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PatternParser.EOF, 0); }
		public List<TemporalPatternContext> temporalPattern() {
			return getRuleContexts(TemporalPatternContext.class);
		}
		public TemporalPatternContext temporalPattern(int i) {
			return getRuleContext(TemporalPatternContext.class,i);
		}
		public PatternEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_patternEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterPatternEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitPatternEntry(this);
		}
	}

	public final PatternEntryContext patternEntry() throws RecognitionException {
		PatternEntryContext _localctx = new PatternEntryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_patternEntry);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__9) | (1L << T__10) | (1L << T__29) | (1L << T__42))) != 0)) {
				{
				{
				setState(70);
				temporalPattern();
				setState(71);
				match(T__0);
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(78);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TemporalPatternContext extends ParserRuleContext {
		public GraphProcessingContext graphProcessing() {
			return getRuleContext(GraphProcessingContext.class,0);
		}
		public TriggerComputationContext triggerComputation() {
			return getRuleContext(TriggerComputationContext.class,0);
		}
		public EmissionContext emission() {
			return getRuleContext(EmissionContext.class,0);
		}
		public StreamProcessingContext streamProcessing() {
			return getRuleContext(StreamProcessingContext.class,0);
		}
		public TemporalPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_temporalPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTemporalPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTemporalPattern(this);
		}
	}

	public final TemporalPatternContext temporalPattern() throws RecognitionException {
		TemporalPatternContext _localctx = new TemporalPatternContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_temporalPattern);
		int _la;
		try {
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(80);
					triggerComputation();
					}
				}

				setState(83);
				graphProcessing();
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__37) {
					{
					setState(84);
					emission();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(87);
					triggerComputation();
					}
				}

				setState(90);
				graphProcessing();
				setState(91);
				streamProcessing();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(94);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(93);
					triggerComputation();
					}
				}

				setState(96);
				streamProcessing();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GraphProcessingContext extends ParserRuleContext {
		public List<ComputationContext> computation() {
			return getRuleContexts(ComputationContext.class);
		}
		public ComputationContext computation(int i) {
			return getRuleContext(ComputationContext.class,i);
		}
		public List<SelectionContext> selection() {
			return getRuleContexts(SelectionContext.class);
		}
		public SelectionContext selection(int i) {
			return getRuleContext(SelectionContext.class,i);
		}
		public List<PartitionContext> partition() {
			return getRuleContexts(PartitionContext.class);
		}
		public PartitionContext partition(int i) {
			return getRuleContext(PartitionContext.class,i);
		}
		public TemporalVariableContext temporalVariable() {
			return getRuleContext(TemporalVariableContext.class,0);
		}
		public GraphProcessingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphProcessing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterGraphProcessing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitGraphProcessing(this);
		}
	}

	public final GraphProcessingContext graphProcessing() throws RecognitionException {
		GraphProcessingContext _localctx = new GraphProcessingContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_graphProcessing);
		int _la;
		try {
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				match(T__1);
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(103);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(100);
						computation();
						}
						break;
					case T__6:
						{
						setState(101);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(102);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(107);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				temporalVariable();
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(112);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(109);
						computation();
						}
						break;
					case T__6:
						{
						setState(110);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(111);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(116);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CollectStreamsContext extends ParserRuleContext {
		public List<TemporalVariableContext> temporalVariable() {
			return getRuleContexts(TemporalVariableContext.class);
		}
		public TemporalVariableContext temporalVariable(int i) {
			return getRuleContext(TemporalVariableContext.class,i);
		}
		public CollectStreamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectStreams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterCollectStreams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitCollectStreams(this);
		}
	}

	public final CollectStreamsContext collectStreams() throws RecognitionException {
		CollectStreamsContext _localctx = new CollectStreamsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_collectStreams);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(T__2);
			setState(120);
			temporalVariable();
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(121);
				match(T__3);
				setState(122);
				temporalVariable();
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(128);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StreamProcessingContext extends ParserRuleContext {
		public CollectStreamsContext collectStreams() {
			return getRuleContext(CollectStreamsContext.class,0);
		}
		public ExtractionContext extraction() {
			return getRuleContext(ExtractionContext.class,0);
		}
		public EmissionContext emission() {
			return getRuleContext(EmissionContext.class,0);
		}
		public EvaluationContext evaluation() {
			return getRuleContext(EvaluationContext.class,0);
		}
		public List<OperationContext> operation() {
			return getRuleContexts(OperationContext.class);
		}
		public OperationContext operation(int i) {
			return getRuleContext(OperationContext.class,i);
		}
		public StreamProcessingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamProcessing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterStreamProcessing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitStreamProcessing(this);
		}
	}

	public final StreamProcessingContext streamProcessing() throws RecognitionException {
		StreamProcessingContext _localctx = new StreamProcessingContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_streamProcessing);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(130);
				collectStreams();
				}
				break;
			case T__9:
			case T__10:
				{
				setState(131);
				extraction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(134);
				operation();
				}
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(142);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				{
				setState(140);
				emission();
				}
				break;
			case T__11:
				{
				setState(141);
				evaluation();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComputationContext extends ParserRuleContext {
		public ComputationFunctionContext computationFunction() {
			return getRuleContext(ComputationFunctionContext.class,0);
		}
		public ComputationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterComputation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitComputation(this);
		}
	}

	public final ComputationContext computation() throws RecognitionException {
		ComputationContext _localctx = new ComputationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_computation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(T__5);
			setState(145);
			computationFunction();
			setState(146);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionContext extends ParserRuleContext {
		public SelectionFunctionContext selectionFunction() {
			return getRuleContext(SelectionFunctionContext.class,0);
		}
		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterSelection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitSelection(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_selection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(T__6);
			setState(149);
			selectionFunction();
			setState(150);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartitionContext extends ParserRuleContext {
		public PartitionFunctionContext partitionFunction() {
			return getRuleContext(PartitionFunctionContext.class,0);
		}
		public PartitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterPartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitPartition(this);
		}
	}

	public final PartitionContext partition() throws RecognitionException {
		PartitionContext _localctx = new PartitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_partition);
		try {
			setState(160);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				match(T__7);
				setState(153);
				partitionFunction();
				setState(154);
				match(T__4);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(156);
				match(T__8);
				setState(157);
				partitionFunction();
				setState(158);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExtractionContext extends ParserRuleContext {
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public ExtractionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extraction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterExtraction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitExtraction(this);
		}
	}

	public final ExtractionContext extraction() throws RecognitionException {
		ExtractionContext _localctx = new ExtractionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_extraction);
		int _la;
		try {
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(162);
				match(T__9);
				setState(163);
				label();
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(164);
					match(T__3);
					setState(165);
					label();
					}
					}
					setState(170);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(171);
				match(T__4);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(T__10);
				setState(174);
				label();
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(175);
					match(T__3);
					setState(176);
					label();
					}
					}
					setState(181);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(182);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EvaluationContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(PatternParser.Operator, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public FireEventContext fireEvent() {
			return getRuleContext(FireEventContext.class,0);
		}
		public EvaluationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterEvaluation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitEvaluation(this);
		}
	}

	public final EvaluationContext evaluation() throws RecognitionException {
		EvaluationContext _localctx = new EvaluationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_evaluation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(T__11);
			setState(187);
			match(Operator);
			setState(188);
			match(T__3);
			setState(189);
			value();
			setState(190);
			match(T__3);
			setState(191);
			fireEvent();
			setState(192);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperationContext extends ParserRuleContext {
		public OperationFunctionContext operationFunction() {
			return getRuleContext(OperationFunctionContext.class,0);
		}
		public OperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterOperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitOperation(this);
		}
	}

	public final OperationContext operation() throws RecognitionException {
		OperationContext _localctx = new OperationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_operation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(T__12);
			setState(195);
			operationFunction();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComputationFunctionContext extends ParserRuleContext {
		public FunctionNameContext functionName() {
			return getRuleContext(FunctionNameContext.class,0);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ComputationFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computationFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterComputationFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitComputationFunction(this);
		}
	}

	public final ComputationFunctionContext computationFunction() throws RecognitionException {
		ComputationFunctionContext _localctx = new ComputationFunctionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_computationFunction);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			functionName();
			setState(198);
			match(T__3);
			setState(199);
			label();
			setState(204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(200);
				match(T__3);
				setState(201);
				label();
				}
				}
				setState(206);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(207);
				match(T__13);
				setState(213);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(208);
						value();
						setState(209);
						match(T__3);
						}
						} 
					}
					setState(215);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				setState(216);
				value();
				setState(217);
				match(T__14);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionFunctionContext extends ParserRuleContext {
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public EdgeSelectionContext edgeSelection() {
			return getRuleContext(EdgeSelectionContext.class,0);
		}
		public SelectionFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterSelectionFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitSelectionFunction(this);
		}
	}

	public final SelectionFunctionContext selectionFunction() throws RecognitionException {
		SelectionFunctionContext _localctx = new SelectionFunctionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_selectionFunction);
		try {
			setState(225);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
			case T__41:
			case T__42:
			case NOT:
			case Litterals:
				enterOuterAlt(_localctx, 1);
				{
				setState(221);
				logicalExpression();
				setState(222);
				edgeSelection();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EdgeSelectionContext extends ParserRuleContext {
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public EdgeSelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edgeSelection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterEdgeSelection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitEdgeSelection(this);
		}
	}

	public final EdgeSelectionContext edgeSelection() throws RecognitionException {
		EdgeSelectionContext _localctx = new EdgeSelectionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_edgeSelection);
		try {
			setState(234);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(227);
				match(T__15);
				setState(230);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__16:
				case T__41:
				case T__42:
				case NOT:
				case Litterals:
					{
					setState(228);
					logicalExpression();
					}
					break;
				case T__14:
					{
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(232);
				match(T__14);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalExpressionContext extends ParserRuleContext {
		public List<BooleanAndExpressionContext> booleanAndExpression() {
			return getRuleContexts(BooleanAndExpressionContext.class);
		}
		public BooleanAndExpressionContext booleanAndExpression(int i) {
			return getRuleContext(BooleanAndExpressionContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(PatternParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(PatternParser.OR, i);
		}
		public LogicalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterLogicalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitLogicalExpression(this);
		}
	}

	public final LogicalExpressionContext logicalExpression() throws RecognitionException {
		LogicalExpressionContext _localctx = new LogicalExpressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_logicalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			booleanAndExpression();
			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(237);
				match(OR);
				setState(238);
				booleanAndExpression();
				}
				}
				setState(243);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanAndExpressionContext extends ParserRuleContext {
		public List<UnaryExpressionContext> unaryExpression() {
			return getRuleContexts(UnaryExpressionContext.class);
		}
		public UnaryExpressionContext unaryExpression(int i) {
			return getRuleContext(UnaryExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(PatternParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(PatternParser.AND, i);
		}
		public BooleanAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterBooleanAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitBooleanAndExpression(this);
		}
	}

	public final BooleanAndExpressionContext booleanAndExpression() throws RecognitionException {
		BooleanAndExpressionContext _localctx = new BooleanAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_booleanAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			unaryExpression();
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(245);
				match(AND);
				setState(246);
				unaryExpression();
				}
				}
				setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(PatternParser.NOT, 0); }
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitUnaryExpression(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_unaryExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(252);
				match(NOT);
				}
			}

			setState(255);
			primaryExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryExpressionContext extends ParserRuleContext {
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public BoolPredicateContext boolPredicate() {
			return getRuleContext(BoolPredicateContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitPrimaryExpression(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_primaryExpression);
		try {
			setState(262);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				match(T__16);
				setState(258);
				logicalExpression();
				setState(259);
				match(T__4);
				}
				break;
			case T__41:
			case T__42:
			case Litterals:
				enterOuterAlt(_localctx, 2);
				{
				setState(261);
				boolPredicate();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolPredicateContext extends ParserRuleContext {
		public OperandsContext leftOp;
		public OperandsContext rightOp;
		public TerminalNode Operator() { return getToken(PatternParser.Operator, 0); }
		public List<OperandsContext> operands() {
			return getRuleContexts(OperandsContext.class);
		}
		public OperandsContext operands(int i) {
			return getRuleContext(OperandsContext.class,i);
		}
		public BoolPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterBoolPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitBoolPredicate(this);
		}
	}

	public final BoolPredicateContext boolPredicate() throws RecognitionException {
		BoolPredicateContext _localctx = new BoolPredicateContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_boolPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
			((BoolPredicateContext)_localctx).leftOp = operands();
			setState(265);
			match(Operator);
			setState(266);
			((BoolPredicateContext)_localctx).rightOp = operands();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandsContext extends ParserRuleContext {
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public TemporalVariableContext temporalVariable() {
			return getRuleContext(TemporalVariableContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public OperandsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operands; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterOperands(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitOperands(this);
		}
	}

	public final OperandsContext operands() throws RecognitionException {
		OperandsContext _localctx = new OperandsContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_operands);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Litterals:
				{
				setState(268);
				label();
				}
				break;
			case T__42:
				{
				setState(269);
				temporalVariable();
				}
				break;
			case T__41:
				{
				setState(270);
				value();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartitionFunctionContext extends ParserRuleContext {
		public List<TemporalVariableContext> temporalVariable() {
			return getRuleContexts(TemporalVariableContext.class);
		}
		public TemporalVariableContext temporalVariable(int i) {
			return getRuleContext(TemporalVariableContext.class,i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public PartitionFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partitionFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterPartitionFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitPartitionFunction(this);
		}
	}

	public final PartitionFunctionContext partitionFunction() throws RecognitionException {
		PartitionFunctionContext _localctx = new PartitionFunctionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_partitionFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__42 || _la==Litterals) {
				{
				setState(275);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__42:
					{
					setState(273);
					temporalVariable();
					}
					break;
				case Litterals:
					{
					setState(274);
					label();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(277);
					match(T__3);
					setState(280);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__42:
						{
						setState(278);
						temporalVariable();
						}
						break;
					case Litterals:
						{
						setState(279);
						label();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					setState(286);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperationFunctionContext extends ParserRuleContext {
		public FunctionNameContext functionName() {
			return getRuleContext(FunctionNameContext.class,0);
		}
		public List<TupleFieldContext> tupleField() {
			return getRuleContexts(TupleFieldContext.class);
		}
		public TupleFieldContext tupleField(int i) {
			return getRuleContext(TupleFieldContext.class,i);
		}
		public OneFieldOperationAliasContext oneFieldOperationAlias() {
			return getRuleContext(OneFieldOperationAliasContext.class,0);
		}
		public OperationFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterOperationFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitOperationFunction(this);
		}
	}

	public final OperationFunctionContext operationFunction() throws RecognitionException {
		OperationFunctionContext _localctx = new OperationFunctionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_operationFunction);
		int _la;
		try {
			setState(324);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__17:
			case T__18:
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 1);
				{
				setState(289);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(290);
				match(T__16);
				setState(291);
				functionName();
				setState(300);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Litterals) {
					{
					setState(292);
					tupleField();
					setState(297);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(293);
						match(T__3);
						setState(294);
						tupleField();
						}
						}
						setState(299);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(302);
				match(T__4);
				}
				break;
			case T__21:
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				_la = _input.LA(1);
				if ( !(_la==T__21 || _la==T__22) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(305);
				match(T__16);
				setState(306);
				tupleField();
				setState(311);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(307);
					match(T__3);
					setState(308);
					tupleField();
					}
					}
					setState(313);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(314);
				match(T__4);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(316);
				match(T__23);
				}
				break;
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
				enterOuterAlt(_localctx, 4);
				{
				setState(317);
				oneFieldOperationAlias();
				setState(322);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__16) {
					{
					setState(318);
					match(T__16);
					setState(319);
					tupleField();
					setState(320);
					match(T__4);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OneFieldOperationAliasContext extends ParserRuleContext {
		public OneFieldOperationAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneFieldOperationAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterOneFieldOperationAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitOneFieldOperationAlias(this);
		}
	}

	public final OneFieldOperationAliasContext oneFieldOperationAlias() throws RecognitionException {
		OneFieldOperationAliasContext _localctx = new OneFieldOperationAliasContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_oneFieldOperationAlias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerComputationContext extends ParserRuleContext {
		public TriggerInputContext triggerInput() {
			return getRuleContext(TriggerInputContext.class,0);
		}
		public TriggerTemporalContext triggerTemporal() {
			return getRuleContext(TriggerTemporalContext.class,0);
		}
		public TriggerSensitivityContext triggerSensitivity() {
			return getRuleContext(TriggerSensitivityContext.class,0);
		}
		public TriggerComputationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerComputation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTriggerComputation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTriggerComputation(this);
		}
	}

	public final TriggerComputationContext triggerComputation() throws RecognitionException {
		TriggerComputationContext _localctx = new TriggerComputationContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_triggerComputation);
		try {
			setState(331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(328);
				triggerInput();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(329);
				triggerTemporal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(330);
				triggerSensitivity();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerInputContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public List<BoolPredicateContext> boolPredicate() {
			return getRuleContexts(BoolPredicateContext.class);
		}
		public BoolPredicateContext boolPredicate(int i) {
			return getRuleContext(BoolPredicateContext.class,i);
		}
		public TriggerInputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerInput; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTriggerInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTriggerInput(this);
		}
	}

	public final TriggerInputContext triggerInput() throws RecognitionException {
		TriggerInputContext _localctx = new TriggerInputContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_triggerInput);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			match(T__29);
			setState(334);
			_la = _input.LA(1);
			if ( !(_la==T__30 || _la==T__31) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(335);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(336);
			match(T__35);
			setState(337);
			variable();
			setState(346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(338);
				match(T__36);
				setState(342);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__41) | (1L << T__42) | (1L << Litterals))) != 0)) {
					{
					{
					setState(339);
					boolPredicate();
					}
					}
					setState(344);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(345);
				match(T__14);
				}
			}

			setState(348);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerTemporalContext extends ParserRuleContext {
		public TerminalNode Timeunit() { return getToken(PatternParser.Timeunit, 0); }
		public TriggerTemporalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerTemporal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTriggerTemporal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTriggerTemporal(this);
		}
	}

	public final TriggerTemporalContext triggerTemporal() throws RecognitionException {
		TriggerTemporalContext _localctx = new TriggerTemporalContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_triggerTemporal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(T__29);
			setState(351);
			match(Timeunit);
			setState(352);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerSensitivityContext extends ParserRuleContext {
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public TriggerSensitivityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerSensitivity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTriggerSensitivity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTriggerSensitivity(this);
		}
	}

	public final TriggerSensitivityContext triggerSensitivity() throws RecognitionException {
		TriggerSensitivityContext _localctx = new TriggerSensitivityContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_triggerSensitivity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			match(T__29);
			setState(355);
			variable();
			setState(360);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(356);
				match(T__3);
				setState(357);
				variable();
				}
				}
				setState(362);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(363);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmissionContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public EmissionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emission; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterEmission(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitEmission(this);
		}
	}

	public final EmissionContext emission() throws RecognitionException {
		EmissionContext _localctx = new EmissionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_emission);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			match(T__37);
			setState(366);
			variable();
			setState(367);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TemporalVariableContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode Timeunit() { return getToken(PatternParser.Timeunit, 0); }
		public TemporalVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_temporalVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTemporalVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTemporalVariable(this);
		}
	}

	public final TemporalVariableContext temporalVariable() throws RecognitionException {
		TemporalVariableContext _localctx = new TemporalVariableContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_temporalVariable);
		int _la;
		try {
			setState(381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(369);
				variable();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(370);
				variable();
				setState(371);
				match(Timeunit);
				setState(372);
				match(T__38);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(374);
				variable();
				setState(376);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(375);
					match(T__39);
					}
				}

				setState(378);
				match(T__40);
				setState(379);
				match(Timeunit);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionNameContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitFunctionName(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(Litterals);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabelContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitLabel(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			match(Litterals);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			match(T__41);
			setState(388);
			match(Litterals);
			setState(389);
			match(T__41);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(T__42);
			setState(392);
			match(Litterals);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FireEventContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public FireEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fireEvent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterFireEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitFireEvent(this);
		}
	}

	public final FireEventContext fireEvent() throws RecognitionException {
		FireEventContext _localctx = new FireEventContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_fireEvent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
			match(T__43);
			setState(395);
			match(Litterals);
			setState(396);
			match(T__43);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TupleFieldContext extends ParserRuleContext {
		public List<TerminalNode> Litterals() { return getTokens(PatternParser.Litterals); }
		public TerminalNode Litterals(int i) {
			return getToken(PatternParser.Litterals, i);
		}
		public TupleFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterTupleField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitTupleField(this);
		}
	}

	public final TupleFieldContext tupleField() throws RecognitionException {
		TupleFieldContext _localctx = new TupleFieldContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_tupleField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(Litterals);
			setState(403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(399);
				match(T__12);
				setState(400);
				match(Litterals);
				}
				}
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\65\u0199\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\2\7\2L\n\2\f\2\16\2O\13\2\3\2\3\2\3"+
		"\3\5\3T\n\3\3\3\3\3\5\3X\n\3\3\3\5\3[\n\3\3\3\3\3\3\3\3\3\5\3a\n\3\3\3"+
		"\5\3d\n\3\3\4\3\4\3\4\3\4\7\4j\n\4\f\4\16\4m\13\4\3\4\3\4\3\4\3\4\7\4"+
		"s\n\4\f\4\16\4v\13\4\5\4x\n\4\3\5\3\5\3\5\3\5\7\5~\n\5\f\5\16\5\u0081"+
		"\13\5\3\5\3\5\3\6\3\6\5\6\u0087\n\6\3\6\7\6\u008a\n\6\f\6\16\6\u008d\13"+
		"\6\3\6\3\6\5\6\u0091\n\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\5\t\u00a3\n\t\3\n\3\n\3\n\3\n\7\n\u00a9\n\n\f\n\16"+
		"\n\u00ac\13\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u00b4\n\n\f\n\16\n\u00b7\13"+
		"\n\3\n\3\n\5\n\u00bb\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\r\7\r\u00cd\n\r\f\r\16\r\u00d0\13\r\3\r\3\r\3"+
		"\r\3\r\7\r\u00d6\n\r\f\r\16\r\u00d9\13\r\3\r\3\r\3\r\5\r\u00de\n\r\3\16"+
		"\3\16\3\16\3\16\5\16\u00e4\n\16\3\17\3\17\3\17\5\17\u00e9\n\17\3\17\3"+
		"\17\5\17\u00ed\n\17\3\20\3\20\3\20\7\20\u00f2\n\20\f\20\16\20\u00f5\13"+
		"\20\3\21\3\21\3\21\7\21\u00fa\n\21\f\21\16\21\u00fd\13\21\3\22\5\22\u0100"+
		"\n\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\5\23\u0109\n\23\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\5\25\u0112\n\25\3\26\3\26\5\26\u0116\n\26\3\26\3"+
		"\26\3\26\5\26\u011b\n\26\7\26\u011d\n\26\f\26\16\26\u0120\13\26\5\26\u0122"+
		"\n\26\3\27\3\27\3\27\3\27\3\27\3\27\7\27\u012a\n\27\f\27\16\27\u012d\13"+
		"\27\5\27\u012f\n\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\7\27\u0138\n\27"+
		"\f\27\16\27\u013b\13\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0145"+
		"\n\27\5\27\u0147\n\27\3\30\3\30\3\31\3\31\3\31\5\31\u014e\n\31\3\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\7\32\u0157\n\32\f\32\16\32\u015a\13\32\3"+
		"\32\5\32\u015d\n\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34"+
		"\7\34\u0169\n\34\f\34\16\34\u016c\13\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\3\36\3\36\3\36\3\36\3\36\3\36\3\36\5\36\u017b\n\36\3\36\3\36\3\36\5\36"+
		"\u0180\n\36\3\37\3\37\3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$"+
		"\3$\7$\u0194\n$\f$\16$\u0197\13$\3$\2\2%\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(*,.\60\62\64\668:<>@BDF\2\7\3\2\24\27\3\2\30\31\3\2\33"+
		"\37\3\2!\"\3\2#%\2\u01ab\2M\3\2\2\2\4c\3\2\2\2\6w\3\2\2\2\by\3\2\2\2\n"+
		"\u0086\3\2\2\2\f\u0092\3\2\2\2\16\u0096\3\2\2\2\20\u00a2\3\2\2\2\22\u00ba"+
		"\3\2\2\2\24\u00bc\3\2\2\2\26\u00c4\3\2\2\2\30\u00c7\3\2\2\2\32\u00e3\3"+
		"\2\2\2\34\u00ec\3\2\2\2\36\u00ee\3\2\2\2 \u00f6\3\2\2\2\"\u00ff\3\2\2"+
		"\2$\u0108\3\2\2\2&\u010a\3\2\2\2(\u0111\3\2\2\2*\u0121\3\2\2\2,\u0146"+
		"\3\2\2\2.\u0148\3\2\2\2\60\u014d\3\2\2\2\62\u014f\3\2\2\2\64\u0160\3\2"+
		"\2\2\66\u0164\3\2\2\28\u016f\3\2\2\2:\u017f\3\2\2\2<\u0181\3\2\2\2>\u0183"+
		"\3\2\2\2@\u0185\3\2\2\2B\u0189\3\2\2\2D\u018c\3\2\2\2F\u0190\3\2\2\2H"+
		"I\5\4\3\2IJ\7\3\2\2JL\3\2\2\2KH\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2"+
		"NP\3\2\2\2OM\3\2\2\2PQ\7\2\2\3Q\3\3\2\2\2RT\5\60\31\2SR\3\2\2\2ST\3\2"+
		"\2\2TU\3\2\2\2UW\5\6\4\2VX\58\35\2WV\3\2\2\2WX\3\2\2\2Xd\3\2\2\2Y[\5\60"+
		"\31\2ZY\3\2\2\2Z[\3\2\2\2[\\\3\2\2\2\\]\5\6\4\2]^\5\n\6\2^d\3\2\2\2_a"+
		"\5\60\31\2`_\3\2\2\2`a\3\2\2\2ab\3\2\2\2bd\5\n\6\2cS\3\2\2\2cZ\3\2\2\2"+
		"c`\3\2\2\2d\5\3\2\2\2ek\7\4\2\2fj\5\f\7\2gj\5\16\b\2hj\5\20\t\2if\3\2"+
		"\2\2ig\3\2\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2lx\3\2\2\2mk\3\2"+
		"\2\2nt\5:\36\2os\5\f\7\2ps\5\16\b\2qs\5\20\t\2ro\3\2\2\2rp\3\2\2\2rq\3"+
		"\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2ux\3\2\2\2vt\3\2\2\2we\3\2\2\2wn\3"+
		"\2\2\2x\7\3\2\2\2yz\7\5\2\2z\177\5:\36\2{|\7\6\2\2|~\5:\36\2}{\3\2\2\2"+
		"~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0082\3\2\2\2\u0081"+
		"\177\3\2\2\2\u0082\u0083\7\7\2\2\u0083\t\3\2\2\2\u0084\u0087\5\b\5\2\u0085"+
		"\u0087\5\22\n\2\u0086\u0084\3\2\2\2\u0086\u0085\3\2\2\2\u0087\u008b\3"+
		"\2\2\2\u0088\u008a\5\26\f\2\u0089\u0088\3\2\2\2\u008a\u008d\3\2\2\2\u008b"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u0090\3\2\2\2\u008d\u008b\3\2"+
		"\2\2\u008e\u0091\58\35\2\u008f\u0091\5\24\13\2\u0090\u008e\3\2\2\2\u0090"+
		"\u008f\3\2\2\2\u0091\13\3\2\2\2\u0092\u0093\7\b\2\2\u0093\u0094\5\30\r"+
		"\2\u0094\u0095\7\7\2\2\u0095\r\3\2\2\2\u0096\u0097\7\t\2\2\u0097\u0098"+
		"\5\32\16\2\u0098\u0099\7\7\2\2\u0099\17\3\2\2\2\u009a\u009b\7\n\2\2\u009b"+
		"\u009c\5*\26\2\u009c\u009d\7\7\2\2\u009d\u00a3\3\2\2\2\u009e\u009f\7\13"+
		"\2\2\u009f\u00a0\5*\26\2\u00a0\u00a1\7\7\2\2\u00a1\u00a3\3\2\2\2\u00a2"+
		"\u009a\3\2\2\2\u00a2\u009e\3\2\2\2\u00a3\21\3\2\2\2\u00a4\u00a5\7\f\2"+
		"\2\u00a5\u00aa\5> \2\u00a6\u00a7\7\6\2\2\u00a7\u00a9\5> \2\u00a8\u00a6"+
		"\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab"+
		"\u00ad\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00ae\7\7\2\2\u00ae\u00bb\3\2"+
		"\2\2\u00af\u00b0\7\r\2\2\u00b0\u00b5\5> \2\u00b1\u00b2\7\6\2\2\u00b2\u00b4"+
		"\5> \2\u00b3\u00b1\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5"+
		"\u00b6\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7\7"+
		"\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00a4\3\2\2\2\u00ba\u00af\3\2\2\2\u00bb"+
		"\23\3\2\2\2\u00bc\u00bd\7\16\2\2\u00bd\u00be\7/\2\2\u00be\u00bf\7\6\2"+
		"\2\u00bf\u00c0\5@!\2\u00c0\u00c1\7\6\2\2\u00c1\u00c2\5D#\2\u00c2\u00c3"+
		"\7\7\2\2\u00c3\25\3\2\2\2\u00c4\u00c5\7\17\2\2\u00c5\u00c6\5,\27\2\u00c6"+
		"\27\3\2\2\2\u00c7\u00c8\5<\37\2\u00c8\u00c9\7\6\2\2\u00c9\u00ce\5> \2"+
		"\u00ca\u00cb\7\6\2\2\u00cb\u00cd\5> \2\u00cc\u00ca\3\2\2\2\u00cd\u00d0"+
		"\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00dd\3\2\2\2\u00d0"+
		"\u00ce\3\2\2\2\u00d1\u00d7\7\20\2\2\u00d2\u00d3\5@!\2\u00d3\u00d4\7\6"+
		"\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d2\3\2\2\2\u00d6\u00d9\3\2\2\2\u00d7"+
		"\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00da\3\2\2\2\u00d9\u00d7\3\2"+
		"\2\2\u00da\u00db\5@!\2\u00db\u00dc\7\21\2\2\u00dc\u00de\3\2\2\2\u00dd"+
		"\u00d1\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\31\3\2\2\2\u00df\u00e0\5\36\20"+
		"\2\u00e0\u00e1\5\34\17\2\u00e1\u00e4\3\2\2\2\u00e2\u00e4\3\2\2\2\u00e3"+
		"\u00df\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4\33\3\2\2\2\u00e5\u00e8\7\22\2"+
		"\2\u00e6\u00e9\5\36\20\2\u00e7\u00e9\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e8"+
		"\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00ed\7\21\2\2\u00eb\u00ed\3"+
		"\2\2\2\u00ec\u00e5\3\2\2\2\u00ec\u00eb\3\2\2\2\u00ed\35\3\2\2\2\u00ee"+
		"\u00f3\5 \21\2\u00ef\u00f0\7\62\2\2\u00f0\u00f2\5 \21\2\u00f1\u00ef\3"+
		"\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\37\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00fb\5\"\22\2\u00f7\u00f8\7\61"+
		"\2\2\u00f8\u00fa\5\"\22\2\u00f9\u00f7\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb"+
		"\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc!\3\2\2\2\u00fd\u00fb\3\2\2\2"+
		"\u00fe\u0100\7\60\2\2\u00ff\u00fe\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u0101"+
		"\3\2\2\2\u0101\u0102\5$\23\2\u0102#\3\2\2\2\u0103\u0104\7\23\2\2\u0104"+
		"\u0105\5\36\20\2\u0105\u0106\7\7\2\2\u0106\u0109\3\2\2\2\u0107\u0109\5"+
		"&\24\2\u0108\u0103\3\2\2\2\u0108\u0107\3\2\2\2\u0109%\3\2\2\2\u010a\u010b"+
		"\5(\25\2\u010b\u010c\7/\2\2\u010c\u010d\5(\25\2\u010d\'\3\2\2\2\u010e"+
		"\u0112\5> \2\u010f\u0112\5:\36\2\u0110\u0112\5@!\2\u0111\u010e\3\2\2\2"+
		"\u0111\u010f\3\2\2\2\u0111\u0110\3\2\2\2\u0112)\3\2\2\2\u0113\u0116\5"+
		":\36\2\u0114\u0116\5> \2\u0115\u0113\3\2\2\2\u0115\u0114\3\2\2\2\u0116"+
		"\u011e\3\2\2\2\u0117\u011a\7\6\2\2\u0118\u011b\5:\36\2\u0119\u011b\5>"+
		" \2\u011a\u0118\3\2\2\2\u011a\u0119\3\2\2\2\u011b\u011d\3\2\2\2\u011c"+
		"\u0117\3\2\2\2\u011d\u0120\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011f\3\2"+
		"\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0115\3\2\2\2\u0121"+
		"\u0122\3\2\2\2\u0122+\3\2\2\2\u0123\u0124\t\2\2\2\u0124\u0125\7\23\2\2"+
		"\u0125\u012e\5<\37\2\u0126\u012b\5F$\2\u0127\u0128\7\6\2\2\u0128\u012a"+
		"\5F$\2\u0129\u0127\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012b"+
		"\u012c\3\2\2\2\u012c\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u0126\3\2"+
		"\2\2\u012e\u012f\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0131\7\7\2\2\u0131"+
		"\u0147\3\2\2\2\u0132\u0133\t\3\2\2\u0133\u0134\7\23\2\2\u0134\u0139\5"+
		"F$\2\u0135\u0136\7\6\2\2\u0136\u0138\5F$\2\u0137\u0135\3\2\2\2\u0138\u013b"+
		"\3\2\2\2\u0139\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013c\3\2\2\2\u013b"+
		"\u0139\3\2\2\2\u013c\u013d\7\7\2\2\u013d\u0147\3\2\2\2\u013e\u0147\7\32"+
		"\2\2\u013f\u0144\5.\30\2\u0140\u0141\7\23\2\2\u0141\u0142\5F$\2\u0142"+
		"\u0143\7\7\2\2\u0143\u0145\3\2\2\2\u0144\u0140\3\2\2\2\u0144\u0145\3\2"+
		"\2\2\u0145\u0147\3\2\2\2\u0146\u0123\3\2\2\2\u0146\u0132\3\2\2\2\u0146"+
		"\u013e\3\2\2\2\u0146\u013f\3\2\2\2\u0147-\3\2\2\2\u0148\u0149\t\4\2\2"+
		"\u0149/\3\2\2\2\u014a\u014e\5\62\32\2\u014b\u014e\5\64\33\2\u014c\u014e"+
		"\5\66\34\2\u014d\u014a\3\2\2\2\u014d\u014b\3\2\2\2\u014d\u014c\3\2\2\2"+
		"\u014e\61\3\2\2\2\u014f\u0150\7 \2\2\u0150\u0151\t\5\2\2\u0151\u0152\t"+
		"\6\2\2\u0152\u0153\7&\2\2\u0153\u015c\5B\"\2\u0154\u0158\7\'\2\2\u0155"+
		"\u0157\5&\24\2\u0156\u0155\3\2\2\2\u0157\u015a\3\2\2\2\u0158\u0156\3\2"+
		"\2\2\u0158\u0159\3\2\2\2\u0159\u015b\3\2\2\2\u015a\u0158\3\2\2\2\u015b"+
		"\u015d\7\21\2\2\u015c\u0154\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015e\3"+
		"\2\2\2\u015e\u015f\7\7\2\2\u015f\63\3\2\2\2\u0160\u0161\7 \2\2\u0161\u0162"+
		"\7\63\2\2\u0162\u0163\7\7\2\2\u0163\65\3\2\2\2\u0164\u0165\7 \2\2\u0165"+
		"\u016a\5B\"\2\u0166\u0167\7\6\2\2\u0167\u0169\5B\"\2\u0168\u0166\3\2\2"+
		"\2\u0169\u016c\3\2\2\2\u016a\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016d"+
		"\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016e\7\7\2\2\u016e\67\3\2\2\2\u016f"+
		"\u0170\7(\2\2\u0170\u0171\5B\"\2\u0171\u0172\7\7\2\2\u01729\3\2\2\2\u0173"+
		"\u0180\5B\"\2\u0174\u0175\5B\"\2\u0175\u0176\7\63\2\2\u0176\u0177\7)\2"+
		"\2\u0177\u0180\3\2\2\2\u0178\u017a\5B\"\2\u0179\u017b\7*\2\2\u017a\u0179"+
		"\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017d\7+\2\2\u017d"+
		"\u017e\7\63\2\2\u017e\u0180\3\2\2\2\u017f\u0173\3\2\2\2\u017f\u0174\3"+
		"\2\2\2\u017f\u0178\3\2\2\2\u0180;\3\2\2\2\u0181\u0182\7\64\2\2\u0182="+
		"\3\2\2\2\u0183\u0184\7\64\2\2\u0184?\3\2\2\2\u0185\u0186\7,\2\2\u0186"+
		"\u0187\7\64\2\2\u0187\u0188\7,\2\2\u0188A\3\2\2\2\u0189\u018a\7-\2\2\u018a"+
		"\u018b\7\64\2\2\u018bC\3\2\2\2\u018c\u018d\7.\2\2\u018d\u018e\7\64\2\2"+
		"\u018e\u018f\7.\2\2\u018fE\3\2\2\2\u0190\u0195\7\64\2\2\u0191\u0192\7"+
		"\17\2\2\u0192\u0194\7\64\2\2\u0193\u0191\3\2\2\2\u0194\u0197\3\2\2\2\u0195"+
		"\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196G\3\2\2\2\u0197\u0195\3\2\2\2"+
		"\60MSWZ`cikrtw\177\u0086\u008b\u0090\u00a2\u00aa\u00b5\u00ba\u00ce\u00d7"+
		"\u00dd\u00e3\u00e8\u00ec\u00f3\u00fb\u00ff\u0108\u0111\u0115\u011a\u011e"+
		"\u0121\u012b\u012e\u0139\u0144\u0146\u014d\u0158\u015c\u016a\u017a\u017f"+
		"\u0195";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}