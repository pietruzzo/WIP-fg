// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/pattern/Pattern.g4 by ANTLR 4.8
package shared.antlr4.pattern;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PatternParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

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
		NOT=46, AND=47, OR=48, DOT=49, Litterals=50, Timeunit=51, Number=52, Skip=53;
	public static final int
		RULE_patternEntry = 0, RULE_temporalPattern = 1, RULE_graphProcessing = 2, 
		RULE_collectStreams = 3, RULE_extractStreamProcessing = 4, RULE_collectStreamProcessing = 5, 
		RULE_computation = 6, RULE_selection = 7, RULE_partition = 8, RULE_extraction = 9, 
		RULE_evaluation = 10, RULE_operation = 11, RULE_computationFunction = 12, 
		RULE_computationReturnVariables = 13, RULE_computationParameters = 14, 
		RULE_aliasedParameter = 15, RULE_selectionFunction = 16, RULE_edgeSelection = 17, 
		RULE_logicalExpression = 18, RULE_booleanAndExpression = 19, RULE_unaryExpression = 20, 
		RULE_primaryExpression = 21, RULE_boolPredicate = 22, RULE_operands = 23, 
		RULE_partitionFunction = 24, RULE_operationFunction = 25, RULE_oneFieldOperationAlias = 26, 
		RULE_triggerComputation = 27, RULE_triggerInput = 28, RULE_triggerTemporal = 29, 
		RULE_triggerSensitivity = 30, RULE_emission = 31, RULE_temporalVariable = 32, 
		RULE_functionName = 33, RULE_label = 34, RULE_value = 35, RULE_variable = 36, 
		RULE_fireEvent = 37, RULE_tupleField = 38;
	private static String[] makeRuleNames() {
		return new String[] {
			"patternEntry", "temporalPattern", "graphProcessing", "collectStreams", 
			"extractStreamProcessing", "collectStreamProcessing", "computation", 
			"selection", "partition", "extraction", "evaluation", "operation", "computationFunction", 
			"computationReturnVariables", "computationParameters", "aliasedParameter", 
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
			"'.SubGraphByV('", "'.SubGraphByE('", "'.extractV('", "'.extractE('", 
			"'.evaluate('", "', ['", "']'", "'='", "'EDGE['", "'('", "'.map'", "'.flatmap'", 
			"'.reduce'", "'.filter'", "'.groupby'", "'.Merge'", "'.collect'", "'.avg'", 
			"'.max'", "'.min'", "'.count'", "'.select'", "'.trigger('", "'edge'", 
			"'vertex'", "'addition'", "'deletion'", "'update'", "'as'", "'['", "'.emit('", 
			"'ago'", "'every'", "'within'", "'''", "'$'", "'\"'", null, "'not'", 
			"'and'", "'or'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "Operator", "NOT", 
			"AND", "OR", "DOT", "Litterals", "Timeunit", "Number", "Skip"
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
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__9) | (1L << T__10) | (1L << T__29) | (1L << T__42))) != 0)) {
				{
				{
				setState(78);
				temporalPattern();
				setState(79);
				match(T__0);
				}
				}
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(86);
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
			setState(105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(88);
					triggerComputation();
					}
				}

				setState(91);
				graphProcessing();
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__37) {
					{
					setState(92);
					emission();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
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
				graphProcessing();
				setState(99);
				extractStreamProcessing();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(101);
					triggerComputation();
					}
				}

				setState(104);
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
			setState(125);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(107);
				match(T__1);
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(111);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(108);
						computation();
						}
						break;
					case T__6:
						{
						setState(109);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(110);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 2);
				{
				setState(116);
				temporalVariable();
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) {
					{
					setState(120);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__5:
						{
						setState(117);
						computation();
						}
						break;
					case T__6:
						{
						setState(118);
						selection();
						}
						break;
					case T__7:
					case T__8:
						{
						setState(119);
						partition();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(124);
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
			setState(127);
			match(T__2);
			setState(128);
			temporalVariable();
			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(129);
				match(T__3);
				setState(130);
				temporalVariable();
				}
				}
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(136);
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
			setState(138);
			extraction();
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28))) != 0)) {
				{
				{
				setState(139);
				operation();
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				{
				setState(145);
				emission();
				}
				break;
			case T__11:
				{
				setState(146);
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
			setState(151);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(149);
				collectStreams();
				}
				break;
			case T__9:
			case T__10:
				{
				setState(150);
				extraction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28))) != 0)) {
				{
				{
				setState(153);
				operation();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				{
				setState(159);
				emission();
				}
				break;
			case T__11:
				{
				setState(160);
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
			setState(163);
			match(T__5);
			setState(164);
			computationFunction();
			setState(165);
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
			setState(167);
			match(T__6);
			setState(168);
			selectionFunction();
			setState(169);
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
			setState(179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(171);
				match(T__7);
				setState(172);
				partitionFunction();
				setState(173);
				match(T__4);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(175);
				match(T__8);
				setState(176);
				partitionFunction();
				setState(177);
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
			setState(205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(181);
				match(T__9);
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Litterals) {
					{
					setState(182);
					label();
					}
				}

				setState(189);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(185);
					match(T__3);
					setState(186);
					label();
					}
					}
					setState(191);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(192);
				match(T__4);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(193);
				match(T__10);
				setState(195);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Litterals) {
					{
					setState(194);
					label();
					}
				}

				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(197);
					match(T__3);
					setState(198);
					label();
					}
					}
					setState(203);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(204);
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
			setState(207);
			match(T__11);
			setState(208);
			match(Operator);
			setState(209);
			match(T__3);
			setState(210);
			value();
			setState(211);
			match(T__3);
			setState(212);
			fireEvent();
			setState(213);
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
			setState(215);
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
		public ComputationReturnVariablesContext computationReturnVariables() {
			return getRuleContext(ComputationReturnVariablesContext.class,0);
		}
		public ComputationParametersContext computationParameters() {
			return getRuleContext(ComputationParametersContext.class,0);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			functionName();
			setState(218);
			match(T__3);
			setState(219);
			computationReturnVariables();
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(220);
				computationParameters();
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

	public static class ComputationReturnVariablesContext extends ParserRuleContext {
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public ComputationReturnVariablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computationReturnVariables; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterComputationReturnVariables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitComputationReturnVariables(this);
		}
	}

	public final ComputationReturnVariablesContext computationReturnVariables() throws RecognitionException {
		ComputationReturnVariablesContext _localctx = new ComputationReturnVariablesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_computationReturnVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			variable();
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(224);
				match(T__3);
				setState(225);
				variable();
				}
				}
				setState(230);
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

	public static class ComputationParametersContext extends ParserRuleContext {
		public List<AliasedParameterContext> aliasedParameter() {
			return getRuleContexts(AliasedParameterContext.class);
		}
		public AliasedParameterContext aliasedParameter(int i) {
			return getRuleContext(AliasedParameterContext.class,i);
		}
		public ComputationParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computationParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterComputationParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitComputationParameters(this);
		}
	}

	public final ComputationParametersContext computationParameters() throws RecognitionException {
		ComputationParametersContext _localctx = new ComputationParametersContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_computationParameters);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(T__12);
			setState(237);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(232);
					aliasedParameter();
					setState(233);
					match(T__3);
					}
					} 
				}
				setState(239);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			setState(240);
			aliasedParameter();
			setState(241);
			match(T__13);
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

	public static class AliasedParameterContext extends ParserRuleContext {
		public TerminalNode Litterals() { return getToken(PatternParser.Litterals, 0); }
		public OperandsContext operands() {
			return getRuleContext(OperandsContext.class,0);
		}
		public AliasedParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasedParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).enterAliasedParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatternListener ) ((PatternListener)listener).exitAliasedParameter(this);
		}
	}

	public final AliasedParameterContext aliasedParameter() throws RecognitionException {
		AliasedParameterContext _localctx = new AliasedParameterContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_aliasedParameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(Litterals);
			setState(244);
			match(T__14);
			setState(245);
			operands();
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
		enterRule(_localctx, 32, RULE_selectionFunction);
		try {
			setState(251);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
			case T__41:
			case T__42:
			case NOT:
			case Litterals:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				logicalExpression();
				setState(248);
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
		enterRule(_localctx, 34, RULE_edgeSelection);
		try {
			setState(260);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(253);
				match(T__15);
				setState(256);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__16:
				case T__41:
				case T__42:
				case NOT:
				case Litterals:
					{
					setState(254);
					logicalExpression();
					}
					break;
				case T__13:
					{
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(258);
				match(T__13);
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
		enterRule(_localctx, 36, RULE_logicalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			booleanAndExpression();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(263);
				match(OR);
				setState(264);
				booleanAndExpression();
				}
				}
				setState(269);
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
		enterRule(_localctx, 38, RULE_booleanAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			unaryExpression();
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(271);
				match(AND);
				setState(272);
				unaryExpression();
				}
				}
				setState(277);
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
		enterRule(_localctx, 40, RULE_unaryExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(278);
				match(NOT);
				}
			}

			setState(281);
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
		enterRule(_localctx, 42, RULE_primaryExpression);
		try {
			setState(288);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				enterOuterAlt(_localctx, 1);
				{
				setState(283);
				match(T__16);
				setState(284);
				logicalExpression();
				setState(285);
				match(T__4);
				}
				break;
			case T__41:
			case T__42:
			case Litterals:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
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
		enterRule(_localctx, 44, RULE_boolPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			((BoolPredicateContext)_localctx).leftOp = operands();
			setState(291);
			match(Operator);
			setState(292);
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
		enterRule(_localctx, 46, RULE_operands);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Litterals:
				{
				setState(294);
				label();
				}
				break;
			case T__42:
				{
				setState(295);
				temporalVariable();
				}
				break;
			case T__41:
				{
				setState(296);
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
		enterRule(_localctx, 48, RULE_partitionFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__42 || _la==Litterals) {
				{
				setState(301);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__42:
					{
					setState(299);
					temporalVariable();
					}
					break;
				case Litterals:
					{
					setState(300);
					label();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(310);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(303);
					match(T__3);
					setState(306);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__42:
						{
						setState(304);
						temporalVariable();
						}
						break;
					case Litterals:
						{
						setState(305);
						label();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					setState(312);
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
		enterRule(_localctx, 50, RULE_operationFunction);
		int _la;
		try {
			setState(350);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__17:
			case T__18:
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 1);
				{
				setState(315);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(316);
				match(T__16);
				setState(317);
				functionName();
				setState(326);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Litterals) {
					{
					setState(318);
					tupleField();
					setState(323);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(319);
						match(T__3);
						setState(320);
						tupleField();
						}
						}
						setState(325);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(328);
				match(T__4);
				}
				break;
			case T__21:
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(330);
				_la = _input.LA(1);
				if ( !(_la==T__21 || _la==T__22) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(331);
				match(T__16);
				setState(332);
				tupleField();
				setState(337);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(333);
					match(T__3);
					setState(334);
					tupleField();
					}
					}
					setState(339);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(340);
				match(T__4);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(342);
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
				setState(343);
				oneFieldOperationAlias();
				setState(348);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__16) {
					{
					setState(344);
					match(T__16);
					setState(345);
					tupleField();
					setState(346);
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
		enterRule(_localctx, 52, RULE_oneFieldOperationAlias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
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
		enterRule(_localctx, 54, RULE_triggerComputation);
		try {
			setState(357);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(354);
				triggerInput();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(355);
				triggerTemporal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(356);
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
		enterRule(_localctx, 56, RULE_triggerInput);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			match(T__29);
			setState(360);
			_la = _input.LA(1);
			if ( !(_la==T__30 || _la==T__31) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(361);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(362);
			match(T__35);
			setState(363);
			variable();
			setState(372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(364);
				match(T__36);
				setState(368);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__41) | (1L << T__42) | (1L << Litterals))) != 0)) {
					{
					{
					setState(365);
					boolPredicate();
					}
					}
					setState(370);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(371);
				match(T__13);
				}
			}

			setState(374);
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
		enterRule(_localctx, 58, RULE_triggerTemporal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			match(T__29);
			setState(377);
			match(Timeunit);
			setState(378);
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
		enterRule(_localctx, 60, RULE_triggerSensitivity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			match(T__29);
			setState(381);
			variable();
			setState(386);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(382);
				match(T__3);
				setState(383);
				variable();
				}
				}
				setState(388);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(389);
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
		enterRule(_localctx, 62, RULE_emission);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(T__37);
			setState(392);
			variable();
			setState(393);
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
		enterRule(_localctx, 64, RULE_temporalVariable);
		int _la;
		try {
			setState(407);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(395);
				variable();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(396);
				variable();
				setState(397);
				match(Timeunit);
				setState(398);
				match(T__38);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(400);
				variable();
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(401);
					match(T__39);
					}
				}

				setState(404);
				match(T__40);
				setState(405);
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
		enterRule(_localctx, 66, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
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
		enterRule(_localctx, 68, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
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
		enterRule(_localctx, 70, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			match(T__41);
			setState(414);
			match(Litterals);
			setState(415);
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
		enterRule(_localctx, 72, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			match(T__42);
			setState(418);
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
		enterRule(_localctx, 74, RULE_fireEvent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(T__43);
			setState(421);
			match(Litterals);
			setState(422);
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
		public List<TerminalNode> DOT() { return getTokens(PatternParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(PatternParser.DOT, i);
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
		enterRule(_localctx, 76, RULE_tupleField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(Litterals);
			setState(429);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(425);
				match(DOT);
				setState(426);
				match(Litterals);
				}
				}
				setState(431);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\67\u01b3\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\3\2\3\2\7\2T\n\2"+
		"\f\2\16\2W\13\2\3\2\3\2\3\3\5\3\\\n\3\3\3\3\3\5\3`\n\3\3\3\5\3c\n\3\3"+
		"\3\3\3\3\3\3\3\5\3i\n\3\3\3\5\3l\n\3\3\4\3\4\3\4\3\4\7\4r\n\4\f\4\16\4"+
		"u\13\4\3\4\3\4\3\4\3\4\7\4{\n\4\f\4\16\4~\13\4\5\4\u0080\n\4\3\5\3\5\3"+
		"\5\3\5\7\5\u0086\n\5\f\5\16\5\u0089\13\5\3\5\3\5\3\6\3\6\7\6\u008f\n\6"+
		"\f\6\16\6\u0092\13\6\3\6\3\6\5\6\u0096\n\6\3\7\3\7\5\7\u009a\n\7\3\7\7"+
		"\7\u009d\n\7\f\7\16\7\u00a0\13\7\3\7\3\7\5\7\u00a4\n\7\3\b\3\b\3\b\3\b"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00b6\n\n\3\13\3"+
		"\13\5\13\u00ba\n\13\3\13\3\13\7\13\u00be\n\13\f\13\16\13\u00c1\13\13\3"+
		"\13\3\13\3\13\5\13\u00c6\n\13\3\13\3\13\7\13\u00ca\n\13\f\13\16\13\u00cd"+
		"\13\13\3\13\5\13\u00d0\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\16\3\16\5\16\u00e0\n\16\3\17\3\17\3\17\7\17\u00e5\n\17\f\17"+
		"\16\17\u00e8\13\17\3\20\3\20\3\20\3\20\7\20\u00ee\n\20\f\20\16\20\u00f1"+
		"\13\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\5\22\u00fe"+
		"\n\22\3\23\3\23\3\23\5\23\u0103\n\23\3\23\3\23\5\23\u0107\n\23\3\24\3"+
		"\24\3\24\7\24\u010c\n\24\f\24\16\24\u010f\13\24\3\25\3\25\3\25\7\25\u0114"+
		"\n\25\f\25\16\25\u0117\13\25\3\26\5\26\u011a\n\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\27\5\27\u0123\n\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\5\31"+
		"\u012c\n\31\3\32\3\32\5\32\u0130\n\32\3\32\3\32\3\32\5\32\u0135\n\32\7"+
		"\32\u0137\n\32\f\32\16\32\u013a\13\32\5\32\u013c\n\32\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\7\33\u0144\n\33\f\33\16\33\u0147\13\33\5\33\u0149\n\33\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u0152\n\33\f\33\16\33\u0155\13"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u015f\n\33\5\33\u0161"+
		"\n\33\3\34\3\34\3\35\3\35\3\35\5\35\u0168\n\35\3\36\3\36\3\36\3\36\3\36"+
		"\3\36\3\36\7\36\u0171\n\36\f\36\16\36\u0174\13\36\3\36\5\36\u0177\n\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3 \7 \u0183\n \f \16 \u0186\13"+
		" \3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\5\"\u0195\n\"\3\"\3\""+
		"\3\"\5\"\u019a\n\"\3#\3#\3$\3$\3%\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3\'\3"+
		"(\3(\3(\7(\u01ae\n(\f(\16(\u01b1\13(\3(\2\2)\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLN\2\7\3\2\24\27\3\2\30\31"+
		"\3\2\33\37\3\2!\"\3\2#%\2\u01c5\2U\3\2\2\2\4k\3\2\2\2\6\177\3\2\2\2\b"+
		"\u0081\3\2\2\2\n\u008c\3\2\2\2\f\u0099\3\2\2\2\16\u00a5\3\2\2\2\20\u00a9"+
		"\3\2\2\2\22\u00b5\3\2\2\2\24\u00cf\3\2\2\2\26\u00d1\3\2\2\2\30\u00d9\3"+
		"\2\2\2\32\u00db\3\2\2\2\34\u00e1\3\2\2\2\36\u00e9\3\2\2\2 \u00f5\3\2\2"+
		"\2\"\u00fd\3\2\2\2$\u0106\3\2\2\2&\u0108\3\2\2\2(\u0110\3\2\2\2*\u0119"+
		"\3\2\2\2,\u0122\3\2\2\2.\u0124\3\2\2\2\60\u012b\3\2\2\2\62\u013b\3\2\2"+
		"\2\64\u0160\3\2\2\2\66\u0162\3\2\2\28\u0167\3\2\2\2:\u0169\3\2\2\2<\u017a"+
		"\3\2\2\2>\u017e\3\2\2\2@\u0189\3\2\2\2B\u0199\3\2\2\2D\u019b\3\2\2\2F"+
		"\u019d\3\2\2\2H\u019f\3\2\2\2J\u01a3\3\2\2\2L\u01a6\3\2\2\2N\u01aa\3\2"+
		"\2\2PQ\5\4\3\2QR\7\3\2\2RT\3\2\2\2SP\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2"+
		"\2\2VX\3\2\2\2WU\3\2\2\2XY\7\2\2\3Y\3\3\2\2\2Z\\\58\35\2[Z\3\2\2\2[\\"+
		"\3\2\2\2\\]\3\2\2\2]_\5\6\4\2^`\5@!\2_^\3\2\2\2_`\3\2\2\2`l\3\2\2\2ac"+
		"\58\35\2ba\3\2\2\2bc\3\2\2\2cd\3\2\2\2de\5\6\4\2ef\5\n\6\2fl\3\2\2\2g"+
		"i\58\35\2hg\3\2\2\2hi\3\2\2\2ij\3\2\2\2jl\5\f\7\2k[\3\2\2\2kb\3\2\2\2"+
		"kh\3\2\2\2l\5\3\2\2\2ms\7\4\2\2nr\5\16\b\2or\5\20\t\2pr\5\22\n\2qn\3\2"+
		"\2\2qo\3\2\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2t\u0080\3\2\2\2"+
		"us\3\2\2\2v|\5B\"\2w{\5\16\b\2x{\5\20\t\2y{\5\22\n\2zw\3\2\2\2zx\3\2\2"+
		"\2zy\3\2\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2\177"+
		"m\3\2\2\2\177v\3\2\2\2\u0080\7\3\2\2\2\u0081\u0082\7\5\2\2\u0082\u0087"+
		"\5B\"\2\u0083\u0084\7\6\2\2\u0084\u0086\5B\"\2\u0085\u0083\3\2\2\2\u0086"+
		"\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008a\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u008a\u008b\7\7\2\2\u008b\t\3\2\2\2\u008c\u0090"+
		"\5\24\13\2\u008d\u008f\5\30\r\2\u008e\u008d\3\2\2\2\u008f\u0092\3\2\2"+
		"\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0095\3\2\2\2\u0092\u0090"+
		"\3\2\2\2\u0093\u0096\5@!\2\u0094\u0096\5\26\f\2\u0095\u0093\3\2\2\2\u0095"+
		"\u0094\3\2\2\2\u0096\13\3\2\2\2\u0097\u009a\5\b\5\2\u0098\u009a\5\24\13"+
		"\2\u0099\u0097\3\2\2\2\u0099\u0098\3\2\2\2\u009a\u009e\3\2\2\2\u009b\u009d"+
		"\5\30\r\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3\2\2\2"+
		"\u009e\u009f\3\2\2\2\u009f\u00a3\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a4"+
		"\5@!\2\u00a2\u00a4\5\26\f\2\u00a3\u00a1\3\2\2\2\u00a3\u00a2\3\2\2\2\u00a4"+
		"\r\3\2\2\2\u00a5\u00a6\7\b\2\2\u00a6\u00a7\5\32\16\2\u00a7\u00a8\7\7\2"+
		"\2\u00a8\17\3\2\2\2\u00a9\u00aa\7\t\2\2\u00aa\u00ab\5\"\22\2\u00ab\u00ac"+
		"\7\7\2\2\u00ac\21\3\2\2\2\u00ad\u00ae\7\n\2\2\u00ae\u00af\5\62\32\2\u00af"+
		"\u00b0\7\7\2\2\u00b0\u00b6\3\2\2\2\u00b1\u00b2\7\13\2\2\u00b2\u00b3\5"+
		"\62\32\2\u00b3\u00b4\7\7\2\2\u00b4\u00b6\3\2\2\2\u00b5\u00ad\3\2\2\2\u00b5"+
		"\u00b1\3\2\2\2\u00b6\23\3\2\2\2\u00b7\u00b9\7\f\2\2\u00b8\u00ba\5F$\2"+
		"\u00b9\u00b8\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bf\3\2\2\2\u00bb\u00bc"+
		"\7\6\2\2\u00bc\u00be\5F$\2\u00bd\u00bb\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf"+
		"\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00bf\3\2"+
		"\2\2\u00c2\u00d0\7\7\2\2\u00c3\u00c5\7\r\2\2\u00c4\u00c6\5F$\2\u00c5\u00c4"+
		"\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00cb\3\2\2\2\u00c7\u00c8\7\6\2\2\u00c8"+
		"\u00ca\5F$\2\u00c9\u00c7\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2"+
		"\2\u00cb\u00cc\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00d0"+
		"\7\7\2\2\u00cf\u00b7\3\2\2\2\u00cf\u00c3\3\2\2\2\u00d0\25\3\2\2\2\u00d1"+
		"\u00d2\7\16\2\2\u00d2\u00d3\7/\2\2\u00d3\u00d4\7\6\2\2\u00d4\u00d5\5H"+
		"%\2\u00d5\u00d6\7\6\2\2\u00d6\u00d7\5L\'\2\u00d7\u00d8\7\7\2\2\u00d8\27"+
		"\3\2\2\2\u00d9\u00da\5\64\33\2\u00da\31\3\2\2\2\u00db\u00dc\5D#\2\u00dc"+
		"\u00dd\7\6\2\2\u00dd\u00df\5\34\17\2\u00de\u00e0\5\36\20\2\u00df\u00de"+
		"\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\33\3\2\2\2\u00e1\u00e6\5J&\2\u00e2"+
		"\u00e3\7\6\2\2\u00e3\u00e5\5J&\2\u00e4\u00e2\3\2\2\2\u00e5\u00e8\3\2\2"+
		"\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\35\3\2\2\2\u00e8\u00e6"+
		"\3\2\2\2\u00e9\u00ef\7\17\2\2\u00ea\u00eb\5 \21\2\u00eb\u00ec\7\6\2\2"+
		"\u00ec\u00ee\3\2\2\2\u00ed\u00ea\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed"+
		"\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f2\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2"+
		"\u00f3\5 \21\2\u00f3\u00f4\7\20\2\2\u00f4\37\3\2\2\2\u00f5\u00f6\7\64"+
		"\2\2\u00f6\u00f7\7\21\2\2\u00f7\u00f8\5\60\31\2\u00f8!\3\2\2\2\u00f9\u00fa"+
		"\5&\24\2\u00fa\u00fb\5$\23\2\u00fb\u00fe\3\2\2\2\u00fc\u00fe\3\2\2\2\u00fd"+
		"\u00f9\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe#\3\2\2\2\u00ff\u0102\7\22\2\2"+
		"\u0100\u0103\5&\24\2\u0101\u0103\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0101"+
		"\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0107\7\20\2\2\u0105\u0107\3\2\2\2"+
		"\u0106\u00ff\3\2\2\2\u0106\u0105\3\2\2\2\u0107%\3\2\2\2\u0108\u010d\5"+
		"(\25\2\u0109\u010a\7\62\2\2\u010a\u010c\5(\25\2\u010b\u0109\3\2\2\2\u010c"+
		"\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\'\3\2\2\2"+
		"\u010f\u010d\3\2\2\2\u0110\u0115\5*\26\2\u0111\u0112\7\61\2\2\u0112\u0114"+
		"\5*\26\2\u0113\u0111\3\2\2\2\u0114\u0117\3\2\2\2\u0115\u0113\3\2\2\2\u0115"+
		"\u0116\3\2\2\2\u0116)\3\2\2\2\u0117\u0115\3\2\2\2\u0118\u011a\7\60\2\2"+
		"\u0119\u0118\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c"+
		"\5,\27\2\u011c+\3\2\2\2\u011d\u011e\7\23\2\2\u011e\u011f\5&\24\2\u011f"+
		"\u0120\7\7\2\2\u0120\u0123\3\2\2\2\u0121\u0123\5.\30\2\u0122\u011d\3\2"+
		"\2\2\u0122\u0121\3\2\2\2\u0123-\3\2\2\2\u0124\u0125\5\60\31\2\u0125\u0126"+
		"\7/\2\2\u0126\u0127\5\60\31\2\u0127/\3\2\2\2\u0128\u012c\5F$\2\u0129\u012c"+
		"\5B\"\2\u012a\u012c\5H%\2\u012b\u0128\3\2\2\2\u012b\u0129\3\2\2\2\u012b"+
		"\u012a\3\2\2\2\u012c\61\3\2\2\2\u012d\u0130\5B\"\2\u012e\u0130\5F$\2\u012f"+
		"\u012d\3\2\2\2\u012f\u012e\3\2\2\2\u0130\u0138\3\2\2\2\u0131\u0134\7\6"+
		"\2\2\u0132\u0135\5B\"\2\u0133\u0135\5F$\2\u0134\u0132\3\2\2\2\u0134\u0133"+
		"\3\2\2\2\u0135\u0137\3\2\2\2\u0136\u0131\3\2\2\2\u0137\u013a\3\2\2\2\u0138"+
		"\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013c\3\2\2\2\u013a\u0138\3\2"+
		"\2\2\u013b\u012f\3\2\2\2\u013b\u013c\3\2\2\2\u013c\63\3\2\2\2\u013d\u013e"+
		"\t\2\2\2\u013e\u013f\7\23\2\2\u013f\u0148\5D#\2\u0140\u0145\5N(\2\u0141"+
		"\u0142\7\6\2\2\u0142\u0144\5N(\2\u0143\u0141\3\2\2\2\u0144\u0147\3\2\2"+
		"\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145"+
		"\3\2\2\2\u0148\u0140\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014a\3\2\2\2\u014a"+
		"\u014b\7\7\2\2\u014b\u0161\3\2\2\2\u014c\u014d\t\3\2\2\u014d\u014e\7\23"+
		"\2\2\u014e\u0153\5N(\2\u014f\u0150\7\6\2\2\u0150\u0152\5N(\2\u0151\u014f"+
		"\3\2\2\2\u0152\u0155\3\2\2\2\u0153\u0151\3\2\2\2\u0153\u0154\3\2\2\2\u0154"+
		"\u0156\3\2\2\2\u0155\u0153\3\2\2\2\u0156\u0157\7\7\2\2\u0157\u0161\3\2"+
		"\2\2\u0158\u0161\7\32\2\2\u0159\u015e\5\66\34\2\u015a\u015b\7\23\2\2\u015b"+
		"\u015c\5N(\2\u015c\u015d\7\7\2\2\u015d\u015f\3\2\2\2\u015e\u015a\3\2\2"+
		"\2\u015e\u015f\3\2\2\2\u015f\u0161\3\2\2\2\u0160\u013d\3\2\2\2\u0160\u014c"+
		"\3\2\2\2\u0160\u0158\3\2\2\2\u0160\u0159\3\2\2\2\u0161\65\3\2\2\2\u0162"+
		"\u0163\t\4\2\2\u0163\67\3\2\2\2\u0164\u0168\5:\36\2\u0165\u0168\5<\37"+
		"\2\u0166\u0168\5> \2\u0167\u0164\3\2\2\2\u0167\u0165\3\2\2\2\u0167\u0166"+
		"\3\2\2\2\u01689\3\2\2\2\u0169\u016a\7 \2\2\u016a\u016b\t\5\2\2\u016b\u016c"+
		"\t\6\2\2\u016c\u016d\7&\2\2\u016d\u0176\5J&\2\u016e\u0172\7\'\2\2\u016f"+
		"\u0171\5.\30\2\u0170\u016f\3\2\2\2\u0171\u0174\3\2\2\2\u0172\u0170\3\2"+
		"\2\2\u0172\u0173\3\2\2\2\u0173\u0175\3\2\2\2\u0174\u0172\3\2\2\2\u0175"+
		"\u0177\7\20\2\2\u0176\u016e\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0178\3"+
		"\2\2\2\u0178\u0179\7\7\2\2\u0179;\3\2\2\2\u017a\u017b\7 \2\2\u017b\u017c"+
		"\7\65\2\2\u017c\u017d\7\7\2\2\u017d=\3\2\2\2\u017e\u017f\7 \2\2\u017f"+
		"\u0184\5J&\2\u0180\u0181\7\6\2\2\u0181\u0183\5J&\2\u0182\u0180\3\2\2\2"+
		"\u0183\u0186\3\2\2\2\u0184\u0182\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0187"+
		"\3\2\2\2\u0186\u0184\3\2\2\2\u0187\u0188\7\7\2\2\u0188?\3\2\2\2\u0189"+
		"\u018a\7(\2\2\u018a\u018b\5J&\2\u018b\u018c\7\7\2\2\u018cA\3\2\2\2\u018d"+
		"\u019a\5J&\2\u018e\u018f\5J&\2\u018f\u0190\7\65\2\2\u0190\u0191\7)\2\2"+
		"\u0191\u019a\3\2\2\2\u0192\u0194\5J&\2\u0193\u0195\7*\2\2\u0194\u0193"+
		"\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\7+\2\2\u0197"+
		"\u0198\7\65\2\2\u0198\u019a\3\2\2\2\u0199\u018d\3\2\2\2\u0199\u018e\3"+
		"\2\2\2\u0199\u0192\3\2\2\2\u019aC\3\2\2\2\u019b\u019c\7\64\2\2\u019cE"+
		"\3\2\2\2\u019d\u019e\7\64\2\2\u019eG\3\2\2\2\u019f\u01a0\7,\2\2\u01a0"+
		"\u01a1\7\64\2\2\u01a1\u01a2\7,\2\2\u01a2I\3\2\2\2\u01a3\u01a4\7-\2\2\u01a4"+
		"\u01a5\7\64\2\2\u01a5K\3\2\2\2\u01a6\u01a7\7.\2\2\u01a7\u01a8\7\64\2\2"+
		"\u01a8\u01a9\7.\2\2\u01a9M\3\2\2\2\u01aa\u01af\7\64\2\2\u01ab\u01ac\7"+
		"\63\2\2\u01ac\u01ae\7\64\2\2\u01ad\u01ab\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af"+
		"\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0O\3\2\2\2\u01b1\u01af\3\2\2\2"+
		"\64U[_bhkqsz|\177\u0087\u0090\u0095\u0099\u009e\u00a3\u00b5\u00b9\u00bf"+
		"\u00c5\u00cb\u00cf\u00df\u00e6\u00ef\u00fd\u0102\u0106\u010d\u0115\u0119"+
		"\u0122\u012b\u012f\u0134\u0138\u013b\u0145\u0148\u0153\u015e\u0160\u0167"+
		"\u0172\u0176\u0184\u0194\u0199\u01af";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}