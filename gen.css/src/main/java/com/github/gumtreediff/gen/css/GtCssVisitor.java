package com.github.gumtreediff.gen.css;

import com.github.gumtreediff.io.LineReader;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.decl.*;
import com.helger.css.decl.visit.ICSSVisitor;
import com.helger.css.writer.CSSWriterSettings;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Stack;

public class GtCssVisitor implements ICSSVisitor {

    private TreeContext ctx;

    private Stack<ITree> trees;

    private LineReader lr;

    private ICSSWriterSettings settings;

    private CascadingStyleSheet sheet;

    public GtCssVisitor(CascadingStyleSheet sheet, LineReader lr) throws IOException {
        this.lr = lr;
        this.settings = new CSSWriterSettings();
        this.sheet = sheet;
        this.ctx = new TreeContext();
        this.trees = new Stack<>();
        ITree root = this.ctx.createTree(1, "", "CascadingStyleSheet");
        setLocation(root, sheet);
        this.ctx.setRoot(root);
        this.trees.push(root);
    }

    public TreeContext getTreeContext() {
        return ctx;
    }

    private void setLocation(ITree t, ICSSSourceLocationAware a) {
        CSSSourceLocation l = a.getSourceLocation();
        int pos = lr.positionFor(l.getFirstTokenBeginLineNumber(), l.getFirstTokenBeginColumnNumber());
        int end = lr.positionFor(l.getLastTokenEndLineNumber(), l.getLastTokenEndColumnNumber());
        int length = end - pos + 1;
        t.setPos(pos + 1);
        t.setLength(length);
    }

    @Override
    public void begin() {
    }

    @Override
    public void onImport(@Nonnull CSSImportRule i) {
        //TODO add media nodes
        ITree t = ctx.createTree(8, i.getAsCSSString(settings, 0), "CSSImportRule");
        t.setParentAndUpdateChildren(trees.peek());
        setLocation(t, i);
    }

    @Override
    public void onNamespace(@Nonnull CSSNamespaceRule n) {

    }

    @Override
    public void onDeclaration(@Nonnull CSSDeclaration d) {
        ITree t = ctx.createTree(3, d.getProperty(), "CSSDeclaration");
        t.setParentAndUpdateChildren(trees.peek());
        setLocation(t, d);
        CSSExpression e = d.getExpression();
        ITree c = ctx.createTree(4, e.getAsCSSString(settings, 0), "CSSExpression");
        c.setParentAndUpdateChildren(t);
        setLocation(c, e);
        //TODO handle expression members.
        /*
        CSSExpression expr = aDeclaration.getExpression();
        for (ICSSExpressionMember member : expr.getAllMembers()) {
            ITree m = ctx.createTree(6, member.getAsCSSString(settings, 0), "CSSExpressionMember");
            m.setParentAndUpdateChildren(c);
        }
        */
    }

    @Override
    public void onBeginStyleRule(@Nonnull CSSStyleRule s) {
        ITree t = ctx.createTree(2, "", "CSSStyleRule");
        setLocation(t, s);
        t.setParentAndUpdateChildren(trees.peek());
        trees.push(t);
    }

    @Override
    public void onStyleRuleSelector(@Nonnull CSSSelector s) {
        ITree t = ctx.createTree(5, s.getAsCSSString(settings, 0), "CSSSelector");
        t.setParentAndUpdateChildren(trees.peek());
        setLocation(t, s);
    }

    @Override
    public void onEndStyleRule(@Nonnull CSSStyleRule aStyleRule) {
        trees.pop();
    }

    @Override
    public void onBeginPageRule(@Nonnull CSSPageRule aPageRule) {

    }

    @Override
    public void onBeginPageMarginBlock(@Nonnull CSSPageMarginBlock aPageMarginBlock) {

    }

    @Override
    public void onEndPageMarginBlock(@Nonnull CSSPageMarginBlock aPageMarginBlock) {

    }

    @Override
    public void onEndPageRule(@Nonnull CSSPageRule aPageRule) {

    }

    @Override
    public void onBeginFontFaceRule(@Nonnull CSSFontFaceRule aFontFaceRule) {

    }

    @Override
    public void onEndFontFaceRule(@Nonnull CSSFontFaceRule aFontFaceRule) {

    }

    @Override
    public void onBeginMediaRule(@Nonnull CSSMediaRule aMediaRule) {

    }

    @Override
    public void onEndMediaRule(@Nonnull CSSMediaRule aMediaRule) {

    }

    @Override
    public void onBeginKeyframesRule(@Nonnull CSSKeyframesRule aKeyframesRule) {

    }

    @Override
    public void onBeginKeyframesBlock(@Nonnull CSSKeyframesBlock aKeyframesBlock) {

    }

    @Override
    public void onEndKeyframesBlock(@Nonnull CSSKeyframesBlock aKeyframesBlock) {

    }

    @Override
    public void onEndKeyframesRule(@Nonnull CSSKeyframesRule aKeyframesRule) {

    }

    @Override
    public void onBeginViewportRule(@Nonnull CSSViewportRule aViewportRule) {

    }

    @Override
    public void onEndViewportRule(@Nonnull CSSViewportRule aViewportRule) {

    }

    @Override
    public void onBeginSupportsRule(@Nonnull CSSSupportsRule aSupportsRule) {

    }

    @Override
    public void onEndSupportsRule(@Nonnull CSSSupportsRule aSupportsRule) {

    }

    @Override
    public void onUnknownRule(@Nonnull CSSUnknownRule aUnknownRule) {

    }

    @Override
    public void end() {

    }
}
