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
		RULE_collectStreams = 3, RULE_extractStreamProcessing = 4, RULE_collectStreamProcessing = 5, 
		RULE_computation = 6, RULE_selection = 7, RULE_partition = 8, RULE_extraction = 9, 
		RULE_evaluation = 10, RULE_operation = 11, RULE_computationFunction = 12, 
		RULE_selectionFunction = 13, RULE_edgeSelection = 14, RULE_logicalExpression = 15, 
		RULE_booleanAndExpression = 16, RULE_unaryExpression = 17, RULE_primaryExpression = 18, 
		RULE_boolPredicate = 19, RULE_operands = 20, RULE_partitionFunction = 21, 
		RULE_operationFunction = 22, RULE_oneFieldOperationAlias = 23, RULE_triggerComputation = 24, 
		RULE_triggerInput = 25, RULE_triggerTemporal = 26, RULE_triggerSensitivity = 27, 
		RULE_emission = 28, RULE_temporalVariable = 29, RULE_functionName = 30, 
		RULE_label = 31, RULE_value = 32, RULE_variable = 33, RULE_fireEvent = 34, 
		RULE_tupleField = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"patternEntry", "temporalPattern", "graphProcessing", "collectStreams", 
			"extractStreamProcessing", "collectStreamProcessing", "computation", 
			"selection", "partition", "extraction", "evaluation", "operation", "computationFunction", 
			"selectionFunction", "edgeSelection", "logicalExpression", "booleanAndExpression", 
			"unaryExpression", "primaryExpression", "boolPredicate", "operands", 
			"partitionFunction", "operationFunction", "oneFieldOperationAlias", "triggerComputation", 
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
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__9) | (1L << T__10) | (1L << T__29) | (1L << T__42))) != 0)) {
				{
				{
				setState(72);
				temporalPattern();
				setState(73);
				match(T__0);
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80);
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
		public ExtractStreamProcessingContext extractStreamProcessing() {
			return getRuleContext(ExtractStreamProcessingContext.class,0);
		}
		public CollectStreamProcessingContext collectStreamProcessing() {
			return getRuleContext(CollectStreamProcessingContext.class,0);
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
			setState(99);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(82);
					triggerComputation();
					}
				}

				setState(85);
				graphProcessing();
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__37) {
					{
					setState(86);
					emission();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(89);
					triggerComputation();
					}
				}

				setState(92);
				graphProcessing();
				setState(93);
				extractStreamProcessing();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(95);
					triggerComputation();
					}
				}

				setState(98);
				collectStreamProcessing();
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
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(101);
				match(T__1);
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(105);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(102);
						computation();
						}
						break;
					case T__6:
						{
						setState(103);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(104);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 2);
				{
				setState(110);
				temporalVariable();
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(114);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(111);
						computation();
						}
						break;
					case T__6:
						{
						setState(112);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(113);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(118);
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
			setState(121);
			match(T__2);
			setState(122);
			temporalVariable();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(123);
				match(T__3);
				setState(124);
				temporalVariable();
				}
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(130);
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

	public static class ExtractStreamProcessingContext extends ParserRuleContext {
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
		public ExtractStreamProcessingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extractStreamProcessing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterExtractStreamProcessing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitExtractStreamProcessing(this);
		}
	}

	public final ExtractStreamProcessingContext extractStreamProcessing() throws RecognitionException {
		ExtractStreamProcessingContext _localctx = new ExtractStreamProcessingContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_extractStreamProcessing);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			extraction();
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(133);
				operation();
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				{
				setState(139);
				emission();
				}
				break;
			case T__11:
				{
				setState(140);
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

	public static class CollectStreamProcessingContext extends ParserRuleContext {
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
		public CollectStreamProcessingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectStreamProcessing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterCollectStreamProcessing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitCollectStreamProcessing(this);
		}
	}

	public final CollectStreamProcessingContext collectStreamProcessing() throws RecognitionException {
		CollectStreamProcessingContext _localctx = new CollectStreamProcessingContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_collectStreamProcessing);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(143);
				collectStreams();
				}
				break;
			case T__9:
			case T__10:
				{
				setState(144);
				extraction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(147);
				operation();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(155);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				{
				setState(153);
				emission();
				}
				break;
			case T__11:
				{
				setState(154);
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
		enterRule(_localctx, 12, RULE_computation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(T__5);
			setState(158);
			computationFunction();
			setState(159);
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
		enterRule(_localctx, 14, RULE_selection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			match(T__6);
			setState(162);
			selectionFunction();
			setState(163);
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
		enterRule(_localctx, 16, RULE_partition);
		try {
			setState(173);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(165);
				match(T__7);
				setState(166);
				partitionFunction();
				setState(167);
				match(T__4);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(169);
				match(T__8);
				setState(170);
				partitionFunction();
				setState(171);
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
		enterRule(_localctx, 18, RULE_extraction);
		int _la;
		try {
			setState(197);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				match(T__9);
				setState(176);
				label();
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(177);
					match(T__3);
					setState(178);
					label();
					}
					}
					setState(183);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(184);
				match(T__4);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(186);
				match(T__10);
				setState(187);
				label();
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(188);
					match(T__3);
					setState(189);
					label();
					}
					}
					setState(194);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(195);
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
		enterRule(_localctx, 20, RULE_evaluation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(T__11);
			setState(200);
			match(Operator);
			setState(201);
			match(T__3);
			setState(202);
			value();
			setState(203);
			match(T__3);
			setState(204);
			fireEvent();
			setState(205);
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
		enterRule(_localctx, 22, RULE_operation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(T__12);
			setState(208);
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
		enterRule(_localctx, 24, RULE_computationFunction);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			functionName();
			setState(211);
			match(T__3);
			setState(212);
			label();
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(213);
				match(T__3);
				setState(214);
				label();
				}
				}
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(220);
				match(T__13);
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(221);
						value();
						setState(222);
						match(T__3);
						}
						} 
					}
					setState(228);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				}
				setState(229);
				value();
				setState(230);
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
		enterRule(_localctx, 26, RULE_selectionFunction);
		try {
			setState(238);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
			case T__41:
			case T__42:
			case NOT:
			case Litterals:
				enterOuterAlt(_localctx, 1);
				{
				setState(234);
				logicalExpression();
				setState(235);
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
		enterRule(_localctx, 28, RULE_edgeSelection);
		try {
			setState(247);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				match(T__15);
				setState(243);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__16:
				case T__41:
				case T__42:
				case NOT:
				case Litterals:
					{
					setState(241);
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
				setState(245);
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
		enterRule(_localctx, 30, RULE_logicalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			booleanAndExpression();
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(250);
				match(OR);
				setState(251);
				booleanAndExpression();
				}
				}
				setState(256);
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
		enterRule(_localctx, 32, RULE_booleanAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			unaryExpression();
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(258);
				match(AND);
				setState(259);
				unaryExpression();
				}
				}
				setState(264);
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
		enterRule(_localctx, 34, RULE_unaryExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(265);
				match(NOT);
				}
			}

			setState(268);
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
		enterRule(_localctx, 36, RULE_primaryExpression);
		try {
			setState(275);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				enterOuterAlt(_localctx, 1);
				{
				setState(270);
				match(T__16);
				setState(271);
				logicalExpression();
				setState(272);
				match(T__4);
				}
				break;
			case T__41:
			case T__42:
			case Litterals:
				enterOuterAlt(_localctx, 2);
				{
				setState(274);
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
		enterRule(_localctx, 38, RULE_boolPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			((BoolPredicateContext)_localctx).leftOp = operands();
			setState(278);
			match(Operator);
			setState(279);
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
		enterRule(_localctx, 40, RULE_operands);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Litterals:
				{
				setState(281);
				label();
				}
				break;
			case T__42:
				{
				setState(282);
				temporalVariable();
				}
				break;
			case T__41:
				{
				setState(283);
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
		enterRule(_localctx, 42, RULE_partitionFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__42 || _la==Litterals) {
				{
				setState(288);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__42:
					{
					setState(286);
					temporalVariable();
					}
					break;
				case Litterals:
					{
					setState(287);
					label();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(297);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(290);
					match(T__3);
					setState(293);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__42:
						{
						setState(291);
						temporalVariable();
						}
						break;
					case Litterals:
						{
						setState(292);
						label();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					setState(299);
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
		enterRule(_localctx, 44, RULE_operationFunction);
		int _la;
		try {
			setState(337);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__17:
			case T__18:
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(303);
				match(T__16);
				setState(304);
				functionName();
				setState(313);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Litterals) {
					{
					setState(305);
					tupleField();
					setState(310);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(306);
						match(T__3);
						setState(307);
						tupleField();
						}
						}
						setState(312);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(315);
				match(T__4);
				}
				break;
			case T__21:
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(317);
				_la = _input.LA(1);
				if ( !(_la==T__21 || _la==T__22) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(318);
				match(T__16);
				setState(319);
				tupleField();
				setState(324);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(320);
					match(T__3);
					setState(321);
					tupleField();
					}
					}
					setState(326);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(327);
				match(T__4);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(329);
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
				setState(330);
				oneFieldOperationAlias();
				setState(335);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__16) {
					{
					setState(331);
					match(T__16);
					setState(332);
					tupleField();
					setState(333);
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
		enterRule(_localctx, 46, RULE_oneFieldOperationAlias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
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
		enterRule(_localctx, 48, RULE_triggerComputation);
		try {
			setState(344);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(341);
				triggerInput();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(342);
				triggerTemporal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(343);
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
		enterRule(_localctx, 50, RULE_triggerInput);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			match(T__29);
			setState(347);
			_la = _input.LA(1);
			if ( !(_la==T__30 || _la==T__31) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(348);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(349);
			match(T__35);
			setState(350);
			variable();
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(351);
				match(T__36);
				setState(355);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__41) | (1L << T__42) | (1L << Litterals))) != 0)) {
					{
					{
					setState(352);
					boolPredicate();
					}
					}
					setState(357);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(358);
				match(T__14);
				}
			}

			setState(361);
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
		enterRule(_localctx, 52, RULE_triggerTemporal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(T__29);
			setState(364);
			match(Timeunit);
			setState(365);
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
		enterRule(_localctx, 54, RULE_triggerSensitivity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(T__29);
			setState(368);
			variable();
			setState(373);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(369);
				match(T__3);
				setState(370);
				variable();
				}
				}
				setState(375);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(376);
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
		enterRule(_localctx, 56, RULE_emission);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			match(T__37);
			setState(379);
			variable();
			setState(380);
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
		enterRule(_localctx, 58, RULE_temporalVariable);
		int _la;
		try {
			setState(394);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(382);
				variable();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(383);
				variable();
				setState(384);
				match(Timeunit);
				setState(385);
				match(T__38);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(387);
				variable();
				setState(389);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(388);
					match(T__39);
					}
				}

				setState(391);
				match(T__40);
				setState(392);
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
		enterRule(_localctx, 60, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
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
		enterRule(_localctx, 62, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
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
		enterRule(_localctx, 64, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(T__41);
			setState(401);
			match(Litterals);
			setState(402);
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
		enterRule(_localctx, 66, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(T__42);
			setState(405);
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
		enterRule(_localctx, 68, RULE_fireEvent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			match(T__43);
			setState(408);
			match(Litterals);
			setState(409);
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
		enterRule(_localctx, 70, RULE_tupleField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(Litterals);
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(412);
				match(T__12);
				setState(413);
				match(Litterals);
				}
				}
				setState(418);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\65\u01a6\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\2\7\2N\n\2\f\2\16\2Q\13\2\3\2"+
		"\3\2\3\3\5\3V\n\3\3\3\3\3\5\3Z\n\3\3\3\5\3]\n\3\3\3\3\3\3\3\3\3\5\3c\n"+
		"\3\3\3\5\3f\n\3\3\4\3\4\3\4\3\4\7\4l\n\4\f\4\16\4o\13\4\3\4\3\4\3\4\3"+
		"\4\7\4u\n\4\f\4\16\4x\13\4\5\4z\n\4\3\5\3\5\3\5\3\5\7\5\u0080\n\5\f\5"+
		"\16\5\u0083\13\5\3\5\3\5\3\6\3\6\7\6\u0089\n\6\f\6\16\6\u008c\13\6\3\6"+
		"\3\6\5\6\u0090\n\6\3\7\3\7\5\7\u0094\n\7\3\7\7\7\u0097\n\7\f\7\16\7\u009a"+
		"\13\7\3\7\3\7\5\7\u009e\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\5\n\u00b0\n\n\3\13\3\13\3\13\3\13\7\13\u00b6\n"+
		"\13\f\13\16\13\u00b9\13\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u00c1\n"+
		"\13\f\13\16\13\u00c4\13\13\3\13\3\13\5\13\u00c8\n\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\7\16\u00da\n\16\f"+
		"\16\16\16\u00dd\13\16\3\16\3\16\3\16\3\16\7\16\u00e3\n\16\f\16\16\16\u00e6"+
		"\13\16\3\16\3\16\3\16\5\16\u00eb\n\16\3\17\3\17\3\17\3\17\5\17\u00f1\n"+
		"\17\3\20\3\20\3\20\5\20\u00f6\n\20\3\20\3\20\5\20\u00fa\n\20\3\21\3\21"+
		"\3\21\7\21\u00ff\n\21\f\21\16\21\u0102\13\21\3\22\3\22\3\22\7\22\u0107"+
		"\n\22\f\22\16\22\u010a\13\22\3\23\5\23\u010d\n\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\24\5\24\u0116\n\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\5\26"+
		"\u011f\n\26\3\27\3\27\5\27\u0123\n\27\3\27\3\27\3\27\5\27\u0128\n\27\7"+
		"\27\u012a\n\27\f\27\16\27\u012d\13\27\5\27\u012f\n\27\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\7\30\u0137\n\30\f\30\16\30\u013a\13\30\5\30\u013c\n\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\30\7\30\u0145\n\30\f\30\16\30\u0148\13"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0152\n\30\5\30\u0154"+
		"\n\30\3\31\3\31\3\32\3\32\3\32\5\32\u015b\n\32\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\7\33\u0164\n\33\f\33\16\33\u0167\13\33\3\33\5\33\u016a\n\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\7\35\u0176\n\35\f\35"+
		"\16\35\u0179\13\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\5\37\u0188\n\37\3\37\3\37\3\37\5\37\u018d\n\37\3 \3 \3!"+
		"\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\7%\u01a1\n%\f%\16%\u01a4"+
		"\13%\3%\2\2&\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:<>@BDFH\2\7\3\2\24\27\3\2\30\31\3\2\33\37\3\2!\"\3\2#%\2\u01b9\2"+
		"O\3\2\2\2\4e\3\2\2\2\6y\3\2\2\2\b{\3\2\2\2\n\u0086\3\2\2\2\f\u0093\3\2"+
		"\2\2\16\u009f\3\2\2\2\20\u00a3\3\2\2\2\22\u00af\3\2\2\2\24\u00c7\3\2\2"+
		"\2\26\u00c9\3\2\2\2\30\u00d1\3\2\2\2\32\u00d4\3\2\2\2\34\u00f0\3\2\2\2"+
		"\36\u00f9\3\2\2\2 \u00fb\3\2\2\2\"\u0103\3\2\2\2$\u010c\3\2\2\2&\u0115"+
		"\3\2\2\2(\u0117\3\2\2\2*\u011e\3\2\2\2,\u012e\3\2\2\2.\u0153\3\2\2\2\60"+
		"\u0155\3\2\2\2\62\u015a\3\2\2\2\64\u015c\3\2\2\2\66\u016d\3\2\2\28\u0171"+
		"\3\2\2\2:\u017c\3\2\2\2<\u018c\3\2\2\2>\u018e\3\2\2\2@\u0190\3\2\2\2B"+
		"\u0192\3\2\2\2D\u0196\3\2\2\2F\u0199\3\2\2\2H\u019d\3\2\2\2JK\5\4\3\2"+
		"KL\7\3\2\2LN\3\2\2\2MJ\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3\2\2\2"+
		"QO\3\2\2\2RS\7\2\2\3S\3\3\2\2\2TV\5\62\32\2UT\3\2\2\2UV\3\2\2\2VW\3\2"+
		"\2\2WY\5\6\4\2XZ\5:\36\2YX\3\2\2\2YZ\3\2\2\2Zf\3\2\2\2[]\5\62\32\2\\["+
		"\3\2\2\2\\]\3\2\2\2]^\3\2\2\2^_\5\6\4\2_`\5\n\6\2`f\3\2\2\2ac\5\62\32"+
		"\2ba\3\2\2\2bc\3\2\2\2cd\3\2\2\2df\5\f\7\2eU\3\2\2\2e\\\3\2\2\2eb\3\2"+
		"\2\2f\5\3\2\2\2gm\7\4\2\2hl\5\16\b\2il\5\20\t\2jl\5\22\n\2kh\3\2\2\2k"+
		"i\3\2\2\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2nz\3\2\2\2om\3\2\2\2"+
		"pv\5<\37\2qu\5\16\b\2ru\5\20\t\2su\5\22\n\2tq\3\2\2\2tr\3\2\2\2ts\3\2"+
		"\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2wz\3\2\2\2xv\3\2\2\2yg\3\2\2\2yp\3\2"+
		"\2\2z\7\3\2\2\2{|\7\5\2\2|\u0081\5<\37\2}~\7\6\2\2~\u0080\5<\37\2\177"+
		"}\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0084\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\7\7\2\2\u0085\t\3\2\2\2"+
		"\u0086\u008a\5\24\13\2\u0087\u0089\5\30\r\2\u0088\u0087\3\2\2\2\u0089"+
		"\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008f\3\2"+
		"\2\2\u008c\u008a\3\2\2\2\u008d\u0090\5:\36\2\u008e\u0090\5\26\f\2\u008f"+
		"\u008d\3\2\2\2\u008f\u008e\3\2\2\2\u0090\13\3\2\2\2\u0091\u0094\5\b\5"+
		"\2\u0092\u0094\5\24\13\2\u0093\u0091\3\2\2\2\u0093\u0092\3\2\2\2\u0094"+
		"\u0098\3\2\2\2\u0095\u0097\5\30\r\2\u0096\u0095\3\2\2\2\u0097\u009a\3"+
		"\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009d\3\2\2\2\u009a"+
		"\u0098\3\2\2\2\u009b\u009e\5:\36\2\u009c\u009e\5\26\f\2\u009d\u009b\3"+
		"\2\2\2\u009d\u009c\3\2\2\2\u009e\r\3\2\2\2\u009f\u00a0\7\b\2\2\u00a0\u00a1"+
		"\5\32\16\2\u00a1\u00a2\7\7\2\2\u00a2\17\3\2\2\2\u00a3\u00a4\7\t\2\2\u00a4"+
		"\u00a5\5\34\17\2\u00a5\u00a6\7\7\2\2\u00a6\21\3\2\2\2\u00a7\u00a8\7\n"+
		"\2\2\u00a8\u00a9\5,\27\2\u00a9\u00aa\7\7\2\2\u00aa\u00b0\3\2\2\2\u00ab"+
		"\u00ac\7\13\2\2\u00ac\u00ad\5,\27\2\u00ad\u00ae\7\7\2\2\u00ae\u00b0\3"+
		"\2\2\2\u00af\u00a7\3\2\2\2\u00af\u00ab\3\2\2\2\u00b0\23\3\2\2\2\u00b1"+
		"\u00b2\7\f\2\2\u00b2\u00b7\5@!\2\u00b3\u00b4\7\6\2\2\u00b4\u00b6\5@!\2"+
		"\u00b5\u00b3\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8"+
		"\3\2\2\2\u00b8\u00ba\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba\u00bb\7\7\2\2\u00bb"+
		"\u00c8\3\2\2\2\u00bc\u00bd\7\r\2\2\u00bd\u00c2\5@!\2\u00be\u00bf\7\6\2"+
		"\2\u00bf\u00c1\5@!\2\u00c0\u00be\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0"+
		"\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5"+
		"\u00c6\7\7\2\2\u00c6\u00c8\3\2\2\2\u00c7\u00b1\3\2\2\2\u00c7\u00bc\3\2"+
		"\2\2\u00c8\25\3\2\2\2\u00c9\u00ca\7\16\2\2\u00ca\u00cb\7/\2\2\u00cb\u00cc"+
		"\7\6\2\2\u00cc\u00cd\5B\"\2\u00cd\u00ce\7\6\2\2\u00ce\u00cf\5F$\2\u00cf"+
		"\u00d0\7\7\2\2\u00d0\27\3\2\2\2\u00d1\u00d2\7\17\2\2\u00d2\u00d3\5.\30"+
		"\2\u00d3\31\3\2\2\2\u00d4\u00d5\5> \2\u00d5\u00d6\7\6\2\2\u00d6\u00db"+
		"\5@!\2\u00d7\u00d8\7\6\2\2\u00d8\u00da\5@!\2\u00d9\u00d7\3\2\2\2\u00da"+
		"\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00ea\3\2"+
		"\2\2\u00dd\u00db\3\2\2\2\u00de\u00e4\7\20\2\2\u00df\u00e0\5B\"\2\u00e0"+
		"\u00e1\7\6\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00df\3\2\2\2\u00e3\u00e6\3\2"+
		"\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2\2\2\u00e6"+
		"\u00e4\3\2\2\2\u00e7\u00e8\5B\"\2\u00e8\u00e9\7\21\2\2\u00e9\u00eb\3\2"+
		"\2\2\u00ea\u00de\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\33\3\2\2\2\u00ec\u00ed"+
		"\5 \21\2\u00ed\u00ee\5\36\20\2\u00ee\u00f1\3\2\2\2\u00ef\u00f1\3\2\2\2"+
		"\u00f0\u00ec\3\2\2\2\u00f0\u00ef\3\2\2\2\u00f1\35\3\2\2\2\u00f2\u00f5"+
		"\7\22\2\2\u00f3\u00f6\5 \21\2\u00f4\u00f6\3\2\2\2\u00f5\u00f3\3\2\2\2"+
		"\u00f5\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00fa\7\21\2\2\u00f8\u00fa"+
		"\3\2\2\2\u00f9\u00f2\3\2\2\2\u00f9\u00f8\3\2\2\2\u00fa\37\3\2\2\2\u00fb"+
		"\u0100\5\"\22\2\u00fc\u00fd\7\62\2\2\u00fd\u00ff\5\"\22\2\u00fe\u00fc"+
		"\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u00fe\3\2\2\2\u0100\u0101\3\2\2\2\u0101"+
		"!\3\2\2\2\u0102\u0100\3\2\2\2\u0103\u0108\5$\23\2\u0104\u0105\7\61\2\2"+
		"\u0105\u0107\5$\23\2\u0106\u0104\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106"+
		"\3\2\2\2\u0108\u0109\3\2\2\2\u0109#\3\2\2\2\u010a\u0108\3\2\2\2\u010b"+
		"\u010d\7\60\2\2\u010c\u010b\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010e\3"+
		"\2\2\2\u010e\u010f\5&\24\2\u010f%\3\2\2\2\u0110\u0111\7\23\2\2\u0111\u0112"+
		"\5 \21\2\u0112\u0113\7\7\2\2\u0113\u0116\3\2\2\2\u0114\u0116\5(\25\2\u0115"+
		"\u0110\3\2\2\2\u0115\u0114\3\2\2\2\u0116\'\3\2\2\2\u0117\u0118\5*\26\2"+
		"\u0118\u0119\7/\2\2\u0119\u011a\5*\26\2\u011a)\3\2\2\2\u011b\u011f\5@"+
		"!\2\u011c\u011f\5<\37\2\u011d\u011f\5B\"\2\u011e\u011b\3\2\2\2\u011e\u011c"+
		"\3\2\2\2\u011e\u011d\3\2\2\2\u011f+\3\2\2\2\u0120\u0123\5<\37\2\u0121"+
		"\u0123\5@!\2\u0122\u0120\3\2\2\2\u0122\u0121\3\2\2\2\u0123\u012b\3\2\2"+
		"\2\u0124\u0127\7\6\2\2\u0125\u0128\5<\37\2\u0126\u0128\5@!\2\u0127\u0125"+
		"\3\2\2\2\u0127\u0126\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u0124\3\2\2\2\u012a"+
		"\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012f\3\2"+
		"\2\2\u012d\u012b\3\2\2\2\u012e\u0122\3\2\2\2\u012e\u012f\3\2\2\2\u012f"+
		"-\3\2\2\2\u0130\u0131\t\2\2\2\u0131\u0132\7\23\2\2\u0132\u013b\5> \2\u0133"+
		"\u0138\5H%\2\u0134\u0135\7\6\2\2\u0135\u0137\5H%\2\u0136\u0134\3\2\2\2"+
		"\u0137\u013a\3\2\2\2\u0138\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013c"+
		"\3\2\2\2\u013a\u0138\3\2\2\2\u013b\u0133\3\2\2\2\u013b\u013c\3\2\2\2\u013c"+
		"\u013d\3\2\2\2\u013d\u013e\7\7\2\2\u013e\u0154\3\2\2\2\u013f\u0140\t\3"+
		"\2\2\u0140\u0141\7\23\2\2\u0141\u0146\5H%\2\u0142\u0143\7\6\2\2\u0143"+
		"\u0145\5H%\2\u0144\u0142\3\2\2\2\u0145\u0148\3\2\2\2\u0146\u0144\3\2\2"+
		"\2\u0146\u0147\3\2\2\2\u0147\u0149\3\2\2\2\u0148\u0146\3\2\2\2\u0149\u014a"+
		"\7\7\2\2\u014a\u0154\3\2\2\2\u014b\u0154\7\32\2\2\u014c\u0151\5\60\31"+
		"\2\u014d\u014e\7\23\2\2\u014e\u014f\5H%\2\u014f\u0150\7\7\2\2\u0150\u0152"+
		"\3\2\2\2\u0151\u014d\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0154\3\2\2\2\u0153"+
		"\u0130\3\2\2\2\u0153\u013f\3\2\2\2\u0153\u014b\3\2\2\2\u0153\u014c\3\2"+
		"\2\2\u0154/\3\2\2\2\u0155\u0156\t\4\2\2\u0156\61\3\2\2\2\u0157\u015b\5"+
		"\64\33\2\u0158\u015b\5\66\34\2\u0159\u015b\58\35\2\u015a\u0157\3\2\2\2"+
		"\u015a\u0158\3\2\2\2\u015a\u0159\3\2\2\2\u015b\63\3\2\2\2\u015c\u015d"+
		"\7 \2\2\u015d\u015e\t\5\2\2\u015e\u015f\t\6\2\2\u015f\u0160\7&\2\2\u0160"+
		"\u0169\5D#\2\u0161\u0165\7\'\2\2\u0162\u0164\5(\25\2\u0163\u0162\3\2\2"+
		"\2\u0164\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168"+
		"\3\2\2\2\u0167\u0165\3\2\2\2\u0168\u016a\7\21\2\2\u0169\u0161\3\2\2\2"+
		"\u0169\u016a\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016c\7\7\2\2\u016c\65"+
		"\3\2\2\2\u016d\u016e\7 \2\2\u016e\u016f\7\63\2\2\u016f\u0170\7\7\2\2\u0170"+
		"\67\3\2\2\2\u0171\u0172\7 \2\2\u0172\u0177\5D#\2\u0173\u0174\7\6\2\2\u0174"+
		"\u0176\5D#\2\u0175\u0173\3\2\2\2\u0176\u0179\3\2\2\2\u0177\u0175\3\2\2"+
		"\2\u0177\u0178\3\2\2\2\u0178\u017a\3\2\2\2\u0179\u0177\3\2\2\2\u017a\u017b"+
		"\7\7\2\2\u017b9\3\2\2\2\u017c\u017d\7(\2\2\u017d\u017e\5D#\2\u017e\u017f"+
		"\7\7\2\2\u017f;\3\2\2\2\u0180\u018d\5D#\2\u0181\u0182\5D#\2\u0182\u0183"+
		"\7\63\2\2\u0183\u0184\7)\2\2\u0184\u018d\3\2\2\2\u0185\u0187\5D#\2\u0186"+
		"\u0188\7*\2\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u0189\3\2"+
		"\2\2\u0189\u018a\7+\2\2\u018a\u018b\7\63\2\2\u018b\u018d\3\2\2\2\u018c"+
		"\u0180\3\2\2\2\u018c\u0181\3\2\2\2\u018c\u0185\3\2\2\2\u018d=\3\2\2\2"+
		"\u018e\u018f\7\64\2\2\u018f?\3\2\2\2\u0190\u0191\7\64\2\2\u0191A\3\2\2"+
		"\2\u0192\u0193\7,\2\2\u0193\u0194\7\64\2\2\u0194\u0195\7,\2\2\u0195C\3"+
		"\2\2\2\u0196\u0197\7-\2\2\u0197\u0198\7\64\2\2\u0198E\3\2\2\2\u0199\u019a"+
		"\7.\2\2\u019a\u019b\7\64\2\2\u019b\u019c\7.\2\2\u019cG\3\2\2\2\u019d\u01a2"+
		"\7\64\2\2\u019e\u019f\7\17\2\2\u019f\u01a1\7\64\2\2\u01a0\u019e\3\2\2"+
		"\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3I"+
		"\3\2\2\2\u01a4\u01a2\3\2\2\2\62OUY\\bekmtvy\u0081\u008a\u008f\u0093\u0098"+
		"\u009d\u00af\u00b7\u00c2\u00c7\u00db\u00e4\u00ea\u00f0\u00f5\u00f9\u0100"+
		"\u0108\u010c\u0115\u011e\u0122\u0127\u012b\u012e\u0138\u013b\u0146\u0151"+
		"\u0153\u015a\u0165\u0169\u0177\u0187\u018c\u01a2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}