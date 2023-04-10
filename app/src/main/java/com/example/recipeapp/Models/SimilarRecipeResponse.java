package com.example.recipeapp.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SimilarRecipeResponse implements List<SimilarRecipeResponse> {
    public int id;
    public String title;
    public String imageType;
    public int readyInMinutes;
    public int servings;
    public String sourceUrl;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<SimilarRecipeResponse> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(SimilarRecipeResponse similarRecipeResponse) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends SimilarRecipeResponse> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends SimilarRecipeResponse> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public SimilarRecipeResponse get(int index) {
        return null;
    }

    @Override
    public SimilarRecipeResponse set(int index, SimilarRecipeResponse element) {
        return null;
    }

    @Override
    public void add(int index, SimilarRecipeResponse element) {

    }

    @Override
    public SimilarRecipeResponse remove(int index) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<SimilarRecipeResponse> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<SimilarRecipeResponse> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<SimilarRecipeResponse> subList(int fromIndex, int toIndex) {
        return null;
    }
}
