import { useState } from "react";
import { login, signup } from "../services/authService";
import { Lock, Mail, User } from "lucide-react";
import Alert from "../components/Alert";
import InputField from "../components/InputField";
import PasswordField from "../components/PasswordField";

const Login = () => {
  const [mode, setMode] = useState("login");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  /* ------------------ HELPERS ------------------ */

  const showNotification = (type, message, duration = 3000) => {
    setNotification({ type, message });
    setTimeout(() => setNotification(null), duration);
  };

  const validateEmail = (email) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  const validateField = (name, value) => {
    switch (name) {
      case "name":
        return value.trim().length < 2
          ? "Name must be at least 2 characters"
          : "";

      case "email":
        return !validateEmail(value) ? "Invalid email address" : "";

      case "password":
        return value.length < 6
          ? "Password must be at least 6 characters"
          : "";

      case "confirmPassword":
        return mode === "signup" && value !== formData.password
          ? "Passwords do not match"
          : "";

      default:
        return "";
    }
  };

  /* ------------------ HANDLERS ------------------ */

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    if (touched[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: validateField(name, value),
      }));
    }
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));
    setErrors((prev) => ({
      ...prev,
      [name]: validateField(name, value),
    }));
  };

  const validateForm = () => {
    const newErrors = {};

    if (mode === "signup") {
      newErrors.name = validateField("name", formData.name);
      newErrors.confirmPassword = validateField(
        "confirmPassword",
        formData.confirmPassword
      );
    }

    newErrors.email = validateField("email", formData.email);
    newErrors.password = validateField("password", formData.password);

    setErrors(newErrors);
    setTouched(
      mode === "signup"
        ? { name: true, email: true, password: true, confirmPassword: true }
        : { email: true, password: true }
    );

    return !Object.values(newErrors).some(Boolean);
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;

    setIsLoading(true);

    try {
      if (mode === "login") {
        await login({
          email: formData.email,
          password: formData.password,
        });
      } else {
        await signup({
          email: formData.email,
          password: formData.password,
        });
      }

      showNotification(
        "success",
        mode === "login"
          ? "Successfully logged in!"
          : "Account created successfully!"
      );

      setFormData({
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
      });
      setTouched({});
      setErrors({});
    } catch (error) {
      showNotification(
        "error",
        error.response?.data?.message ||
          error.message ||
          "Something went wrong. Please try again.",
        3000
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSubmit();
  };

  const switchMode = () => {
    setMode(mode === "login" ? "signup" : "login");
    setErrors({});
    setTouched({});
    setNotification(null);
  };

  /* ------------------ UI ------------------ */

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <Alert type={notification?.type} message={notification?.message} />

        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-blue-100 rounded-full mb-4">
              <Lock className="h-8 w-8 text-blue-600" />
            </div>
            <h2 className="text-3xl font-bold text-gray-900">
              {mode === "login" ? "Welcome Back" : "Create Account"}
            </h2>
            <p className="text-gray-600 mt-2">
              {mode === "login"
                ? "Enter your credentials to access your account"
                : "Sign up to get started with your account"}
            </p>
          </div>

          <div className="space-y-6">
            {mode === "signup" && (
              <InputField
                icon={User}
                name="name"
                label="Full Name"
                placeholder="John Doe"
                value={formData.name}
                onChange={handleChange}
                onBlur={handleBlur}
                onKeyPress={handleKeyPress}
                error={errors.name}
                touched={touched.name}
              />
            )}

            <InputField
              icon={Mail}
              name="email"
              type="email"
              label="Email Address"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              onBlur={handleBlur}
              onKeyPress={handleKeyPress}
              error={errors.email}
              touched={touched.email}
            />

            <PasswordField
              label="Password"
              name="password"
              value={formData.password}
              showPassword={showPassword}
              setShowPassword={setShowPassword}
              onChange={handleChange}
              onBlur={handleBlur}
              onKeyPress={handleKeyPress}
              error={errors.password}
              touched={touched.password}
              autoComplete={
                mode === "login" ? "current-password" : "new-password"
              }
            />

            {mode === "signup" && (
              <InputField
                icon={Lock}
                name="confirmPassword"
                type={showPassword ? "text" : "password"}
                label="Confirm Password"
                placeholder="Confirm your password"
                value={formData.confirmPassword}
                onChange={handleChange}
                onBlur={handleBlur}
                onKeyPress={handleKeyPress}
                error={errors.confirmPassword}
                touched={touched.confirmPassword}
              />
            )}

            <button
              onClick={handleSubmit}
              disabled={isLoading}
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
            >
              {isLoading
                ? "Processing..."
                : mode === "login"
                ? "Sign In"
                : "Create Account"}
            </button>
          </div>

          <div className="mt-6 text-center">
            <p className="text-sm text-gray-600">
              {mode === "login"
                ? "Don't have an account? "
                : "Already have an account? "}
              <button
                onClick={switchMode}
                className="font-medium text-blue-600 hover:text-blue-500"
              >
                {mode === "login" ? "Sign up" : "Sign in"}
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
