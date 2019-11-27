// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/input/Commands.g4 by ANTLR 4.7.2
package shared.antlr4.input;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CommandsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, Alphanumerical=11, Number=12, Skip=13;
	public static final int
		RULE_clientCommand = 0, RULE_updateCommand = 1, RULE_vertexUpdate = 2, 
		RULE_edgeUpdate = 3, RULE_labelValues = 4, RULE_vertexType = 5, RULE_updateType = 6, 
		RULE_edgeIdentifier = 7, RULE_identifier = 8, RULE_timestamp = 9, RULE_value = 10, 
		RULE_litterals = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"clientCommand", "updateCommand", "vertexUpdate", "edgeUpdate", "labelValues", 
			"vertexType", "updateType", "edgeIdentifier", "identifier", "timestamp", 
			"value", "litterals"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "'vertex'", "':'", "'edge'", "'='", "'insert'", "'delete'", 
			"'update'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "Alphanumerical", 
			"Number", "Skip"
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
	public String getGrammarFileName() { return "Commands.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CommandsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ClientCommandContext extends ParserRuleContext {
		public UpdateCommandContext updateCommand() {
			return getRuleContext(UpdateCommandContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CommandsParser.EOF, 0); }
		public ClientCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clientCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterClientCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitClientCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitClientCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClientCommandContext clientCommand() throws RecognitionException {
		ClientCommandContext _localctx = new ClientCommandContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_clientCommand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			updateCommand();
			setState(25);
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

	public static class UpdateCommandContext extends ParserRuleContext {
		public LabelValuesContext labelValues() {
			return getRuleContext(LabelValuesContext.class,0);
		}
		public TimestampContext timestamp() {
			return getRuleContext(TimestampContext.class,0);
		}
		public VertexUpdateContext vertexUpdate() {
			return getRuleContext(VertexUpdateContext.class,0);
		}
		public EdgeUpdateContext edgeUpdate() {
			return getRuleContext(EdgeUpdateContext.class,0);
		}
		public UpdateCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterUpdateCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitUpdateCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitUpdateCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateCommandContext updateCommand() throws RecognitionException {
		UpdateCommandContext _localctx = new UpdateCommandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_updateCommand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(27);
				vertexUpdate();
				}
				break;
			case T__3:
				{
				setState(28);
				edgeUpdate();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(31);
			labelValues();
			setState(32);
			match(T__0);
			setState(33);
			timestamp();
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

	public static class VertexUpdateContext extends ParserRuleContext {
		public UpdateTypeContext updateType() {
			return getRuleContext(UpdateTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public VertexUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vertexUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterVertexUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitVertexUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitVertexUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VertexUpdateContext vertexUpdate() throws RecognitionException {
		VertexUpdateContext _localctx = new VertexUpdateContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_vertexUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			match(T__1);
			setState(36);
			updateType();
			setState(37);
			match(T__2);
			setState(38);
			identifier();
			setState(39);
			match(T__0);
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

	public static class EdgeUpdateContext extends ParserRuleContext {
		public UpdateTypeContext updateType() {
			return getRuleContext(UpdateTypeContext.class,0);
		}
		public EdgeIdentifierContext edgeIdentifier() {
			return getRuleContext(EdgeIdentifierContext.class,0);
		}
		public EdgeUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edgeUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterEdgeUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitEdgeUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitEdgeUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EdgeUpdateContext edgeUpdate() throws RecognitionException {
		EdgeUpdateContext _localctx = new EdgeUpdateContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_edgeUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(T__3);
			setState(42);
			updateType();
			setState(43);
			match(T__2);
			setState(44);
			edgeIdentifier();
			setState(45);
			match(T__0);
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

	public static class LabelValuesContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public LabelValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterLabelValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitLabelValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitLabelValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelValuesContext labelValues() throws RecognitionException {
		LabelValuesContext _localctx = new LabelValuesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_labelValues);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			identifier();
			setState(48);
			match(T__4);
			setState(49);
			value();
			{
			setState(50);
			match(T__0);
			setState(51);
			identifier();
			setState(52);
			match(T__4);
			setState(53);
			value();
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

	public static class VertexTypeContext extends ParserRuleContext {
		public VertexTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vertexType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterVertexType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitVertexType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitVertexType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VertexTypeContext vertexType() throws RecognitionException {
		VertexTypeContext _localctx = new VertexTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_vertexType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_la = _input.LA(1);
			if ( !(_la==T__1 || _la==T__3) ) {
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

	public static class UpdateTypeContext extends ParserRuleContext {
		public UpdateTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterUpdateType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitUpdateType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitUpdateType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateTypeContext updateType() throws RecognitionException {
		UpdateTypeContext _localctx = new UpdateTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_updateType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7))) != 0)) ) {
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

	public static class EdgeIdentifierContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public EdgeIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edgeIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterEdgeIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitEdgeIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitEdgeIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EdgeIdentifierContext edgeIdentifier() throws RecognitionException {
		EdgeIdentifierContext _localctx = new EdgeIdentifierContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_edgeIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			identifier();
			setState(60);
			match(T__0);
			setState(61);
			identifier();
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

	public static class IdentifierContext extends ParserRuleContext {
		public LitteralsContext litterals() {
			return getRuleContext(LitteralsContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			litterals();
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

	public static class TimestampContext extends ParserRuleContext {
		public TerminalNode Number() { return getToken(CommandsParser.Number, 0); }
		public TimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitTimestamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimestampContext timestamp() throws RecognitionException {
		TimestampContext _localctx = new TimestampContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_timestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(Number);
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
		public List<LitteralsContext> litterals() {
			return getRuleContexts(LitteralsContext.class);
		}
		public LitteralsContext litterals(int i) {
			return getRuleContext(LitteralsContext.class,i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_value);
		try {
			int _alt;
			setState(80);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Alphanumerical:
			case Number:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				litterals();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				match(T__8);
				setState(74);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(69);
						litterals();
						setState(70);
						match(T__0);
						}
						} 
					}
					setState(76);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				setState(77);
				litterals();
				setState(78);
				match(T__9);
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

	public static class LitteralsContext extends ParserRuleContext {
		public List<TerminalNode> Alphanumerical() { return getTokens(CommandsParser.Alphanumerical); }
		public TerminalNode Alphanumerical(int i) {
			return getToken(CommandsParser.Alphanumerical, i);
		}
		public List<TerminalNode> Number() { return getTokens(CommandsParser.Number); }
		public TerminalNode Number(int i) {
			return getToken(CommandsParser.Number, i);
		}
		public LitteralsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_litterals; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).enterLitterals(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CommandsListener ) ((CommandsListener)listener).exitLitterals(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CommandsVisitor ) return ((CommandsVisitor<? extends T>)visitor).visitLitterals(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LitteralsContext litterals() throws RecognitionException {
		LitteralsContext _localctx = new LitteralsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_litterals);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(82);
				_la = _input.LA(1);
				if ( !(_la==Alphanumerical || _la==Number) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(85); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Alphanumerical || _la==Number );
			litterals = Al
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\17\\\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13"+
		"\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\3\3\3\5\3 \n\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\7\fK\n\f\f\f\16\fN\13\f\3\f\3\f\3\f\5\fS\n\f\3\r\6\rV\n\r\r\r\16\r"+
		"W\3\r\3\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\5\4\2\4\4\6\6\3\2"+
		"\b\n\3\2\r\16\2S\2\32\3\2\2\2\4\37\3\2\2\2\6%\3\2\2\2\b+\3\2\2\2\n\61"+
		"\3\2\2\2\f9\3\2\2\2\16;\3\2\2\2\20=\3\2\2\2\22A\3\2\2\2\24C\3\2\2\2\26"+
		"R\3\2\2\2\30U\3\2\2\2\32\33\5\4\3\2\33\34\7\2\2\3\34\3\3\2\2\2\35 \5\6"+
		"\4\2\36 \5\b\5\2\37\35\3\2\2\2\37\36\3\2\2\2 !\3\2\2\2!\"\5\n\6\2\"#\7"+
		"\3\2\2#$\5\24\13\2$\5\3\2\2\2%&\7\4\2\2&\'\5\16\b\2\'(\7\5\2\2()\5\22"+
		"\n\2)*\7\3\2\2*\7\3\2\2\2+,\7\6\2\2,-\5\16\b\2-.\7\5\2\2./\5\20\t\2/\60"+
		"\7\3\2\2\60\t\3\2\2\2\61\62\5\22\n\2\62\63\7\7\2\2\63\64\5\26\f\2\64\65"+
		"\7\3\2\2\65\66\5\22\n\2\66\67\7\7\2\2\678\5\26\f\28\13\3\2\2\29:\t\2\2"+
		"\2:\r\3\2\2\2;<\t\3\2\2<\17\3\2\2\2=>\5\22\n\2>?\7\3\2\2?@\5\22\n\2@\21"+
		"\3\2\2\2AB\5\30\r\2B\23\3\2\2\2CD\7\16\2\2D\25\3\2\2\2ES\5\30\r\2FL\7"+
		"\13\2\2GH\5\30\r\2HI\7\3\2\2IK\3\2\2\2JG\3\2\2\2KN\3\2\2\2LJ\3\2\2\2L"+
		"M\3\2\2\2MO\3\2\2\2NL\3\2\2\2OP\5\30\r\2PQ\7\f\2\2QS\3\2\2\2RE\3\2\2\2"+
		"RF\3\2\2\2S\27\3\2\2\2TV\t\4\2\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2"+
		"\2XY\3\2\2\2YZ\b\r\1\2Z\31\3\2\2\2\6\37LRW";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}