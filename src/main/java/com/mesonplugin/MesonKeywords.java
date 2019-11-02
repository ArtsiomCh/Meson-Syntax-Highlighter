package com.mesonplugin;

import com.google.common.collect.Sets;
import com.intellij.util.Functions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class MesonKeywords {
  static final Set<String> FUNCTIONS = new HashSet<>();

  static final Set<String> ALL_METHODS = new HashSet<>();
  static final Set<String> MESON_METHODS = new HashSet<>();
  static final Set<String> BUILD_MACHINE_METHODS = new HashSet<>();
  static final Set<String> STRING_METHODS = new HashSet<>();
  static final Set<String> NUMBER_METHODS = new HashSet<>();
  static final Set<String> BOOLEAN_METHODS = new HashSet<>();
  static final Set<String> ARRAY_METHODS = new HashSet<>();
  static final Set<String> DICTIONARY_METHODS = new HashSet<>();

  static final Map<String, Set<String>> FUN2METHODS = new HashMap<>();
  static final Map<String, Set<String>> METHOD2METHODS = new HashMap<>();

  static final List<String> BUILT_IN_OBJECTS = Arrays.asList("meson", "build_machine", "host_machine", "target_machine");

  static {
    // https://github.com/mesonbuild/meson/blob/master/data/syntax-highlighting/vim/syntax/meson.vim
    Collections.addAll(
        FUNCTIONS,
        "add_global_arguments",
        "add_global_link_arguments",
        "add_languages",
        "add_project_arguments",
        "add_project_link_arguments",
        "add_test_setup",
        "alias_target",
        "assert",
        "benchmark",
        "both_libraries",
        "build_target",
        "configuration_data",
        "configure_file",
        "custom_target",
        "declare_dependency",
        "dependency",
        "disabler",
        "environment",
        "error",
        "executable",
        "files",
        "find_library",
        "find_program",
        "generator",
        "get_option",
        "get_variable",
        "import",
        "include_directories",
        "install_data",
        "install_headers",
        "install_man",
        "install_subdir",
        "is_disabler",
        "is_variable",
        "jar",
        "join_paths",
        "library",
        "message",
        "project",
        "run_command",
        "run_target",
        "set_variable",
        "shared_library",
        "shared_module",
        "static_library",
        "subdir",
        "subdir_done",
        "subproject",
        "test",
        "vcs_tag",
        "warning" // "meson", "target_machine", "option", "gettext", "host_machine", "build_machine"
        );

    // https://mesonbuild.com/Reference-manual.html#builtin-objects
    Collections.addAll(
        MESON_METHODS,
        "add_dist_script",
        "add_install_script",
        "backend",
        "build_root",
        "source_root",
        "current_build_dir",
        "current_source_dir",
        "get_cross_property",
        "get_compiler",
        "has_exe_wrapper",
        "install_dependency_manifest",
        "is_cross_build",
        "is_subproject",
        "is_unity",
        "override_find_program",
        "project_version",
        "project_license",
        "project_name",
        "version");
    Collections.addAll(BUILD_MACHINE_METHODS, "cpu_family", "cpu", "system", "endian");
    Collections.addAll(
        STRING_METHODS,
        "contains",
        "endswith",
        "format",
        "join",
        "split",
        "startswith",
        "strip",
        "to_int",
        "to_upper",
        "underscorify",
        "version_compare");
    Collections.addAll(NUMBER_METHODS, "is_even", "is_odd", "to_string");
    Collections.addAll(BOOLEAN_METHODS, "to_int", "to_string");
    Collections.addAll(ARRAY_METHODS, "contains", "get", "length");
    Collections.addAll(DICTIONARY_METHODS, "has_key", "get");

    // todo: keyword arguments
    // https://mesonbuild.com/Reference-manual.html#returned-objects
    final Set<String> compiler_methods =
        new HashSet<>(
            Arrays.asList(
                "alignment",
                "cmd_array",
                "compiles",
                "compute_int",
                "find_library",
                "first_supported_argument",
                "get_define",
                "get_id",
                "get_argument_syntax",
                "get_supported_arguments",
                "get_supported_link_arguments",
                "has_argument",
                "has_link_argument",
                "has_function",
                "check_header",
                "has_header",
                "has_header_symbol",
                "has_member",
                "has_members",
                "has_multi_arguments",
                "has_multi_link_arguments",
                "has_type",
                "links",
                "run",
                "symbols_have_underscore_prefix",
                "sizeof",
                "version",
                "has_function_attribute",
                "get_supported_function_attributes"));
    METHOD2METHODS.put("get_compiler", compiler_methods);

    final Set<String> build_target_methods =
        new HashSet<>(
            Arrays.asList(
                "extract_all_objects", "extract_objects", "full_path", "private_dir_include"));
    FUN2METHODS.put("executable", build_target_methods);
    FUN2METHODS.put("shared_library", build_target_methods);
    FUN2METHODS.put("static_library", build_target_methods);
    FUN2METHODS.put("both_libraries", build_target_methods);
    FUN2METHODS.put("shared_module", build_target_methods);

    final Set<String> configuration_data_methods =
        new HashSet<>(Arrays.asList("get", "get_unquoted", "has", "set10", "set_quoted", "set"));
    FUN2METHODS.put("configuration_data", configuration_data_methods);

    final Set<String> custom_target_methods = new HashSet<>(Arrays.asList("full_path"));
    FUN2METHODS.put("custom_target", custom_target_methods);

    final Set<String> dependency_methods =
        new HashSet<>(
            Arrays.asList(
                "found",
                "name",
                "get_pkgconfig_variable",
                "get_configtool_variable",
                "type_name",
                "version",
                "include_type",
                "as_system",
                "partial_dependency",
                "get_variable"));
    FUN2METHODS.put("dependency", dependency_methods);

    final Set<String> external_program_methods = new HashSet<>(Arrays.asList("found", "path"));
    FUN2METHODS.put("find_program", external_program_methods);

    final Set<String> environment_methods =
        new HashSet<>(Arrays.asList("append", "prepend", "set"));
    FUN2METHODS.put("environment", environment_methods);

    final Set<String> external_library_methods =
        new HashSet<>(Arrays.asList("found", "type_name", "partial_dependency"));
    FUN2METHODS.put("find_library", external_library_methods);

    final Set<String> generator_methods = new HashSet<>(Arrays.asList("process"));
    FUN2METHODS.put("generator", generator_methods);

    final Set<String> subproject_methods = new HashSet<>(Arrays.asList("found", "get_variable"));
    FUN2METHODS.put("subproject", subproject_methods);

    final Set<String> run_command_result_methods =
        new HashSet<>(Arrays.asList("returncode", "stderr", "stdout"));
    final HashSet<String> run_result_methods = new HashSet<>(run_command_result_methods);
    run_result_methods.add("compiled");
    FUN2METHODS.put("run", run_result_methods);
    FUN2METHODS.put("run_command", run_command_result_methods);

    final Set<String> allFunMethods =
        FUN2METHODS.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

    ALL_METHODS.addAll(MESON_METHODS);
    ALL_METHODS.addAll(BUILD_MACHINE_METHODS);
    ALL_METHODS.addAll(STRING_METHODS);
    ALL_METHODS.addAll(NUMBER_METHODS);
    ALL_METHODS.addAll(BOOLEAN_METHODS);
    ALL_METHODS.addAll(ARRAY_METHODS);
    ALL_METHODS.addAll(DICTIONARY_METHODS);
    ALL_METHODS.addAll(allFunMethods);
    ALL_METHODS.addAll(
        METHOD2METHODS.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));

    // todo: check method's returning type
    STRING_METHODS.forEach(strMethod -> METHOD2METHODS.put(strMethod, STRING_METHODS));
    allFunMethods.forEach(methodName -> METHOD2METHODS.put(methodName, STRING_METHODS));

    FUNCTIONS.forEach(funName -> FUN2METHODS.merge(funName, STRING_METHODS, Sets::union));
  }
}
